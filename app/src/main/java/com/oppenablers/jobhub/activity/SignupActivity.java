package com.oppenablers.jobhub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.internal.FallbackServiceBroker;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.oppenablers.jobhub.AuthManager;
import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.Util;
import com.oppenablers.jobhub.api.JobHubClient;
import com.oppenablers.jobhub.databinding.ActivitySignupBinding;
import com.oppenablers.jobhub.model.JobSeeker;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SignupActivity extends AppCompatActivity {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    private ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);

        TextInputLayout nameInput = binding.fullOrCompanyName;
        TextInputLayout birthdayInput = binding.birthday;
        TextInputLayout companyAddressInput = binding.companyAddress;
        TextInputLayout emailInput = binding.email;
        TextInputLayout passwordInput = binding.password;
        TextInputLayout confirmPasswordInput = binding.confirmPassword;

        // Toggle listener to switch form hints
        binding.jobToggle.setOnToggleListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Employer
                nameInput.setHint(getString(R.string.company_name));
                companyAddressInput.setVisibility(View.VISIBLE);
                birthdayInput.setVisibility(View.GONE);
            } else {
                // Job Seeker
                nameInput.setHint(getString(R.string.full_name));
                birthdayInput.setVisibility(View.VISIBLE);
                companyAddressInput.setVisibility(View.GONE);
            }
        });

        birthdayInput.setStartIconOnClickListener(v -> {
            String toFormat = Util.getTextFromTextInputLayout(birthdayInput);
            Date initialDate;
            try {
                initialDate = dateFormat.parse(toFormat);
            } catch (ParseException ignored) {
                initialDate = new Date();
            }

            if (initialDate == null) return;

            MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select birthday")
                    .setSelection(initialDate.getTime())
                    .build();

            picker.addOnPositiveButtonClickListener(selection -> {
                Date date = new Date(selection);
                Util.setTextFromTextInputLayout(birthdayInput, dateFormat.format(date));
            });

            picker.show(getSupportFragmentManager(), null);
        });

        binding.signupButton.setOnClickListener(v -> {
            String name = Util.getTextFromTextInputLayout(nameInput);
            String addressOrBirthday;
            if (binding.jobToggle.isOn()) {
                addressOrBirthday = Util.getTextFromTextInputLayout(companyAddressInput);
            } else {
                addressOrBirthday = Util.getTextFromTextInputLayout(birthdayInput);
            }
            String email = Util.getTextFromTextInputLayout(emailInput);
            String password = Util.getTextFromTextInputLayout(passwordInput);
            String confirmPassword = Util.getTextFromTextInputLayout(confirmPasswordInput);

            binding.ErrorText.setVisibility(View.GONE);  // hide previous errors

            if (name.isEmpty() || addressOrBirthday.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                binding.ErrorText.setText(R.string.signup_all_fields_required);
                binding.ErrorText.setVisibility(View.VISIBLE);
                return;
            }

            String toFormat = Util.getTextFromTextInputLayout(birthdayInput);
            Date birthdayDate = new Date();
            try {
                birthdayDate = dateFormat.parse(toFormat);
                if (birthdayDate != null) {
                    Util.setTextFromTextInputLayout(birthdayInput, dateFormat.format(birthdayDate));
                    birthdayInput.setError("");
                    birthdayInput.setErrorEnabled(false);
                } else {
                    return;
                }
            } catch (ParseException ife) {
                birthdayInput.setError("Unable to parse date");
            }

            if (!password.equals(confirmPassword)) {
                binding.ErrorText.setText(R.string.signup_passwords_not_match);
                binding.ErrorText.setVisibility(View.VISIBLE);
                return;
            }

            Date finalBirthdayDate = birthdayDate;

            AuthManager.signup(email, password).addOnSuccessListener(authResult -> {
                FirebaseUser user = authResult.getUser();

                if (user == null) {
                    Toast.makeText(this, "Failed to create user", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (binding.jobToggle.isOn()) {
                    // TODO employer
                } else {
                    JobSeeker jobSeeker = new JobSeeker(user.getUid(),
                            email,
                            "",
                            name,
                            finalBirthdayDate.getTime(),
                            new ArrayList<>(),
                            new ArrayList<>(),
                            "",
                            "");

                    // what a mess
                    JobHubClient.signUpJobSeeker(jobSeeker, new JobHubClient.JobHubCallbackVoid() {
                        @Override
                        public void onFailure() {
                            Toast.makeText(SignupActivity.this, "Failed to sign up", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess() {
                            // force refresh token with new claims

                            AuthManager.getIdToken(true).addOnSuccessListener(getTokenResult -> {
                                JobHubClient.login(getTokenResult.getToken(), user.getUid(), new JobHubClient.JobHubCallbackVoid() {

                                    @Override
                                    public void onFailure() {

                                    }

                                    @Override
                                    public void onSuccess() {
                                        Intent intent = new Intent(SignupActivity.this, JsNavigatorActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                });
                            });
                        }
                    });
                }
            });
        });
    }
}
