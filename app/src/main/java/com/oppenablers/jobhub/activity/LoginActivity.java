package com.oppenablers.jobhub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.oppenablers.jobhub.AuthManager;
import com.oppenablers.jobhub.Utility;
import com.oppenablers.jobhub.api.JobHubClient;
import com.oppenablers.jobhub.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);

        binding.loginButton.setOnClickListener(v -> {

            String email = Utility.getTextFromTextInputLayout(binding.email);
            String password = Utility.getTextFromTextInputLayout(binding.password);

            AuthManager.login(email, password)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser user = authResult.getUser();

                        if (user != null) {
                            user.getIdToken(false)
                                    .addOnSuccessListener(getTokenResult ->


                                            JobHubClient.login(getTokenResult.getToken(), user.getUid(), new JobHubClient.JobHubCallbackVoid() {
                                                @Override
                                                public void onFailure() {
                                                    Toast.makeText(LoginActivity.this, "Log in failed", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onSuccess() {

                                                    String userType = (String) getTokenResult.getClaims().get("user_type");

                                                    if (userType.contentEquals("jobseeker")) {
                                                        Intent intent = new Intent(LoginActivity.this, JsNavigatorActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                    } else if (userType.contentEquals("employer")) {
                                                        Intent intent = new Intent(LoginActivity.this, EmpNavigatorActivity.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                        startActivity(intent);
                                                    }

                                                }
                                            }));
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Wrong credentials", Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
