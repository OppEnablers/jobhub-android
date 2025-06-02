package com.oppenablers.jobhub.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.oppenablers.jobhub.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EditText nameInput = binding.fullOrCompanyName;
        EditText addressOrBirthdayInput = binding.companyAddressOrBirthday;
        EditText emailInput = binding.emailEditText;
        EditText confirmEmailInput = binding.confirmEmailEditText;
        EditText passwordInput = binding.passwordEditText;
        EditText confirmPasswordInput = binding.confirmPasswordEditText;

        // Toggle listener to switch form hints
        binding.jobToggle.setOnToggleListener((buttonView, isChecked) -> {
            nameInput.setText("");
            addressOrBirthdayInput.setText("");
            emailInput.setText("");
            confirmEmailInput.setText("");
            passwordInput.setText("");
            confirmPasswordInput.setText("");

            if (isChecked) {
                // Employer
                nameInput.setHint("Company Name");
                addressOrBirthdayInput.setHint("Company Address");
            } else {
                // Job Seeker
                nameInput.setHint("Full Name");
                addressOrBirthdayInput.setHint("Birthday MM/DD/YYYY");
            }
        });

        binding.signupButton.setOnClickListener(v -> {
            String name = binding.fullOrCompanyName.getText().toString().trim();
            String addressOrBirthday = binding.companyAddressOrBirthday.getText().toString().trim();
            String email = binding.emailEditText.getText().toString().trim();
            String confirmEmail = binding.confirmEmailEditText.getText().toString().trim();
            String password = binding.passwordEditText.getText().toString();
            String confirmPassword = binding.confirmPasswordEditText.getText().toString();

            binding.ErrorText.setVisibility(View.GONE);  // hide previous errors

            if (name.isEmpty() || addressOrBirthday.isEmpty() || email.isEmpty()
                    || confirmEmail.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                binding.ErrorText.setText("All fields are required");
                binding.ErrorText.setVisibility(View.VISIBLE);
                return;
            }

            if (!email.equals(confirmEmail)) {
                binding.ErrorText.setText("Emails do not match");
                binding.ErrorText.setVisibility(View.VISIBLE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                binding.ErrorText.setText("Passwords do not match");
                binding.ErrorText.setVisibility(View.VISIBLE);
                return;
            }

            String userType = binding.jobToggle.isOn() ? "employer" : "jobseeker";

//            Need to connect firebase here

            Toast.makeText(SignupActivity.this, userType + " signed up successfully", Toast.LENGTH_SHORT).show();
        });
    }
}
