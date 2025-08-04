package com.oppenablers.jobhub.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.oppenablers.jobhub.AuthManager;
import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.api.AuthInterceptor;
import com.oppenablers.jobhub.api.JobHubClient;
import com.oppenablers.jobhub.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private static void logoClick(View v) {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preferences_key), Context.MODE_PRIVATE);
        updateClientHostName(sharedPreferences);

        if (AuthManager.isLoggedIn()) {
            JobHubClient.setUserId(AuthManager.getCurrentUser().getUid());
            AuthManager.getIdToken(false).addOnSuccessListener(getTokenResult -> {
                AuthInterceptor.setToken(getTokenResult.getToken());

                String userType = (String) getTokenResult.getClaims().get("user_type");

                if (userType == null) return;

                FirebaseFirestore.getInstance()
                        .collection(userType + "s")
                        .document(AuthManager.getCurrentUser().getUid()).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            setUserName(documentSnapshot.get("name", String.class));

                            if (userType.contentEquals("jobseeker")) {
                                Intent intent = new Intent(MainActivity.this, JsNavigatorActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else if (userType.contentEquals("employer")) {
                                Intent intent = new Intent(MainActivity.this, EmpNavigatorActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            });
        }

        binding.loginButton.setOnClickListener(v -> {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        });

        binding.signUpButton.setOnClickListener(v -> {
            Intent signupIntent = new Intent(this, SignupActivity.class);
            startActivity(signupIntent);
        });

        binding.jobHubLogo.setOnClickListener(new View.OnClickListener() {
            int tapCount = 0;
            long lastMillis = 0;

            @Override
            public void onClick(View v) {

                long millisDiff = System.currentTimeMillis() - lastMillis;
                lastMillis = System.currentTimeMillis();

                Log.d("Main", "millis diff: " + millisDiff);

                if (millisDiff > 500)
                    tapCount = 0;

                tapCount++;

                Log.d("Main", "tap count: " + tapCount);

                if (tapCount == 7) {
                    tapCount = 0;

                    Context context = MainActivity.this;

                    FrameLayout frameLayout = new FrameLayout(context);
                    TextInputLayout til = new TextInputLayout(context);
                    TextInputEditText tiet = new TextInputEditText(context);
                    til.addView(tiet);
                    til.setHint("Host name");
                    til.setPlaceholderText(JobHubClient.getHostName());
                    frameLayout.addView(til);

                    int paddingPx = (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 16,
                            getResources().getDisplayMetrics());
                    frameLayout.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);

                    AlertDialog alert = new MaterialAlertDialogBuilder(context)
                            .setTitle("Set server host")
                            .setMessage("Set the server host name for JobHub to use")
                            .setView(frameLayout)
                            .setPositiveButton("OK", (dialog, which) -> {
                                if (tiet.getText() != null) {
                                    String newHostName = tiet.getText().toString().trim();
                                    if (!newHostName.isEmpty()) {
                                        sharedPreferences.edit().putString("host_name", newHostName)
                                                .apply();
                                        updateClientHostName(sharedPreferences);
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> {
                            })
                            .create();
                    alert.show();
                }
            }
        });
    }

    private void updateClientHostName(SharedPreferences sharedPreferences) {
        JobHubClient.setHostName(sharedPreferences.getString("host_name", "localhost"));
    }

    private void setUserName(String userName) {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        prefs.edit()
                .putString("currentUserName", userName)
                .apply();
    }
}