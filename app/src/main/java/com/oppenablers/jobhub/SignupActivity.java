package com.oppenablers.jobhub;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.oppenablers.jobhub.databinding.ActivitySignupBinding;
import com.oppenablers.mariatoggle.widget.LabeledSwitch;

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
            String name = nameInput.getText().toString().trim();
            String addressOrBirthday = addressOrBirthdayInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String confirmEmail = confirmEmailInput.getText().toString().trim();
            String password = passwordInput.getText().toString();
            String confirmPassword = confirmPasswordInput.getText().toString();

            if (name.isEmpty() || addressOrBirthday.isEmpty() || email.isEmpty() ||
                    confirmEmail.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Missing fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.equals(confirmEmail)) {
                Toast.makeText(SignupActivity.this, "Emails do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            String userType = binding.jobToggle.isOn() ? "employer" : "jobseeker";

//            Need to connect firebase here

            Toast.makeText(SignupActivity.this, userType + " signed up successfully", Toast.LENGTH_SHORT).show();
        });
    }
}
