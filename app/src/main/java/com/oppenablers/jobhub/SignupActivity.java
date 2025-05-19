package com.oppenablers.jobhub;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity {

    private Switch jobToggle;
    private TextView userTypeText;
    private EditText nameInput, addressOrBirthdayInput;
    private EditText emailInput, confirmEmailInput;
    private EditText passwordInput, confirmPasswordInput;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        jobToggle = findViewById(R.id.jobToggle);
        userTypeText = findViewById(R.id.userTypeText);
        nameInput = findViewById(R.id.fullOrCompanyName);
        addressOrBirthdayInput = findViewById(R.id.companyAddressOrBirthday);
        emailInput = findViewById(R.id.emailEditText);
        confirmEmailInput = findViewById(R.id.confirmEmailEditText);
        passwordInput = findViewById(R.id.passwordEditText);
        confirmPasswordInput = findViewById(R.id.confirmPasswordEditText);
        signupButton = findViewById(R.id.signupButton);

        // Toggle listener to switch form hints
        jobToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Employer
                userTypeText.setText("Employer");
                nameInput.setHint("Company Name");
                addressOrBirthdayInput.setHint("Company Address");
            } else {
                // Job Seeker
                userTypeText.setText("Job Seeker");
                nameInput.setHint("Full Name");
                addressOrBirthdayInput.setHint("Birthday MM/DD/YYYY");
            }
        });

        signupButton.setOnClickListener(v -> {
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

            String userType = jobToggle.isChecked() ? "employer" : "jobseeker";

//            Need to connect firebase here

            Toast.makeText(SignupActivity.this, userType + " signed up successfully", Toast.LENGTH_SHORT).show();
        });
    }
}
