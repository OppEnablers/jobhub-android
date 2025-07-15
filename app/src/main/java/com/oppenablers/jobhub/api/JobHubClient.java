package com.oppenablers.jobhub.api;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.oppenablers.jobhub.model.Employer;
import com.oppenablers.jobhub.model.Job;
import com.oppenablers.jobhub.model.JobSeeker;
import com.oppenablers.jobhub.model.Vacancy;

import java.io.IOException;
import java.util.ArrayList;

import kotlinx.coroutines.JobNode;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JobHubClient {

    private static String hostName = "localhost";
    private static String baseUrl = "https://" + hostName + "/api";
    private static final Handler HANDLER = HandlerCompat.createAsync(Looper.getMainLooper());
    private static final AuthInterceptor INTERCEPTOR = new AuthInterceptor();
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .hostnameVerifier((hostname, session) -> hostname.contentEquals(hostName))
            .addInterceptor(INTERCEPTOR)
            .build();
    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    private static String userId = "";

    public static String getHostName() {
        return hostName;
    }

    public static void setHostName(String hostName) {
        baseUrl = "https://" + hostName + "/api";
        JobHubClient.hostName = hostName;
    }

    public static void ping(JobHubCallbackVoid callback) {
        CLIENT.newCall(get("/ping")).enqueue(createNotifyCallback(callback));
    }

    public static void login(String idToken, String userId, JobHubCallbackVoid callback) {
        setUserId(userId);
        AuthInterceptor.setToken(idToken);
        CLIENT.newCall(get("/auth/login")).enqueue(createNotifyCallback(callback));
    }

    public static void setUserId(String userId) {
        JobHubClient.userId = userId;
    }

    public static void signUpJobSeeker(JobSeeker jobSeeker, JobHubCallbackVoid callback) {
        String jobSeekerJson = GSON.toJson(jobSeeker);
        CLIENT.newCall(post("/auth/signup/jobseeker", jobSeekerJson))
                .enqueue(createNotifyCallback(callback));
    }

    public static void signUpEmployer(Employer employer, JobHubCallbackVoid callback) {
        String employerJson = GSON.toJson(employer);
        CLIENT.newCall(post("/auth/signup/employer", employerJson))
                .enqueue(createNotifyCallback(callback));
    }

    public static void getAccountInfoJobSeeker(JobHubCallback<JobSeeker> callback) {
        CLIENT.newCall(get("/jobseeker/account_info"))
                .enqueue(createNotifyCallback(new TypeToken<>() {
                }, callback));
    }

    public static void updateAccountInfoJobSeeker(JobSeeker accountInfo, JobHubCallbackVoid callback) {
        String accountInfoJson = GSON.toJson(accountInfo);
        CLIENT.newCall(post("/jobseeker/update_info", accountInfoJson))
                .enqueue(createNotifyCallback(callback));
    }

    public static void addVacancy(Vacancy vacancy, JobHubCallbackVoid callback) {
        String vacancyJson = GSON.toJson(vacancy);
        CLIENT.newCall(post("/employer/add_vacancy", vacancyJson))
                .enqueue(createNotifyCallback(callback));
    }

    public static void getVacanciesEmployer(JobHubCallback<ArrayList<Vacancy>> callback) {
        CLIENT.newCall(get("/employer/get_vacancies"))
                .enqueue(createNotifyCallback(new TypeToken<>(){}, callback));
    }

    public static void getJobsJobSeeker(JobHubCallback<ArrayList<Vacancy>> callback) {
        CLIENT.newCall(get("/jobseeker/jobs"))
                .enqueue(createNotifyCallback(new TypeToken<>(){}, callback));
    }

    public static void acceptJobJobSeeker(Vacancy job, JobHubCallbackVoid callback) {
        String jobJson = GSON.toJson(job);
        CLIENT.newCall(post("/jobseeker/accept_job", jobJson))
                .enqueue(createNotifyCallback(callback));
    }

    public static void declineJobJobSeeker(Vacancy job, JobHubCallbackVoid callback) {
        String jobJson = GSON.toJson(job);
        CLIENT.newCall(post("/jobseeker/decline_job", jobJson))
                .enqueue(createNotifyCallback(callback));
    }

    public static void getApplicationsJobSeeker(JobHubCallback<ArrayList<Job>> callback) {
        CLIENT.newCall(get("/jobseeker/applications"))
                .enqueue(createNotifyCallback(new TypeToken<>(){}, callback));
    }

    private static Request post(String endpoint, String body) {
        return startRequest(endpoint)
                .post(RequestBody.create(body, MediaType.parse("application/json")))
                .build();
    }

    private static Request get(String endpoint) {
        return startRequest(endpoint)
                .get()
                .build();
    }

    private static Request.Builder startRequest(String endpoint) {
        return new Request.Builder()
                .url(baseUrl + endpoint)
                .header("user_id", userId);
    }

    public interface JobHubCallback<T> {
        void onFailure();

        void onSuccess(T result);
    }

    public interface JobHubCallbackVoid {
        void onFailure();

        void onSuccess();
    }

    /**
     * this is the stupidest hack i've ever had to write
     */

    private static Callback createNotifyCallback(JobHubCallbackVoid originalCallback) {
        return new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                HANDLER.post(originalCallback::onFailure);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                HANDLER.post(() -> {
                    if (response.body() == null) {
                        originalCallback.onFailure();
                        return;
                    }

                    originalCallback.onSuccess();
                });
            }
        };
    }

    private static <T> Callback createNotifyCallback(TypeToken<T> typeToken, JobHubCallback<T> originalCallback) {
        return new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                HANDLER.post(originalCallback::onFailure);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                HANDLER.post(() -> {
                    try {

                        if (response.body() == null) {
                            originalCallback.onFailure();
                            return;
                        }

                        T result = GSON.fromJson(response.body().string(), typeToken);
                        originalCallback.onSuccess(result);
                    } catch (Exception e) {
                        originalCallback.onFailure();
                    }
                });
            }
        };
    }
}
