package com.oppenablers.jobhub.api;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;

import com.google.gson.Gson;
import com.oppenablers.jobhub.model.Employer;
import com.oppenablers.jobhub.model.JobSeeker;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JobHubClient {

    private static final String HOSTNAME = "10.0.2.2";
    private static final String BASE_URL = "https://" + HOSTNAME + ":7267/api";
    private static final Handler HANDLER = HandlerCompat.createAsync(Looper.getMainLooper());
    private static final AuthInterceptor INTERCEPTOR = new AuthInterceptor();
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .hostnameVerifier((hostname, session) -> hostname.contentEquals(HOSTNAME))
            .addInterceptor(INTERCEPTOR)
            .build();
    private static final Gson GSON = new Gson();

    private static String token;

    public static void login(String idToken, Callback callback) {
        INTERCEPTOR.setToken(idToken);
        CLIENT.newCall(get("/auth/login")).enqueue(createNotifyCallback(callback));
    }

    public static void signUpJobSeeker(JobSeeker jobSeeker, Callback callback) {
        String jobSeekerJson = GSON.toJson(jobSeeker);
        CLIENT.newCall(post("/auth/signup/jobseeker", jobSeekerJson))
                .enqueue(createNotifyCallback(callback));
    }

    public static void signUpEmployer(Employer employer, Callback callback) {
        String jobSeekerJson = GSON.toJson(employer);
        CLIENT.newCall(post("/auth/signup/employer", jobSeekerJson))
                .enqueue(createNotifyCallback(callback));
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
                .url(BASE_URL + endpoint);
    }

    /**
     * this is the stupidest hack i've ever had to write
     */
    private static Callback createNotifyCallback(Callback originalCallback) {
        return new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                HANDLER.post(() -> originalCallback.onFailure(call, e));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                HANDLER.post(() -> {
                    try {
                        originalCallback.onResponse(call, response);
                    } catch (IOException e) {
                        originalCallback.onFailure(call, e);
                    }
                });
            }
        };
    }
}
