package com.oppenablers.jobhub.api;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.GetTokenResult;
import com.oppenablers.jobhub.AuthManager;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private static String token;

    public static void setToken(String token) {
        AuthInterceptor.token = token;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        Request request = chain.request();

        Request.Builder builder = request.newBuilder();
        setAuthHeader(builder, token);

        request = builder.build();

        Response response = chain.proceed(request);

        // for refreshing tokens
        if (response.code() == 401) {
            synchronized (this) {
                Task<GetTokenResult> task = AuthManager.getIdToken(false);
                try {
                    token = Tasks.await(task).getToken();
                    builder = request.newBuilder();
                    setAuthHeader(builder, token);
                    request = builder.build();
                    return chain.proceed(request);
                } catch (ExecutionException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return response;
    }

    private void setAuthHeader(Request.Builder builder, String token) {
        builder.header("Authorization", "Bearer " + token);
    }
}
