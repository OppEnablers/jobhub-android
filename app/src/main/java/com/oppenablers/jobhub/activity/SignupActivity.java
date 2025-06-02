package com.oppenablers.jobhub;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.oppenablers.jobhub.databinding.ActivitySignupBinding;
import com.oppenablers.mariatoggle.widget.LabeledSwitch;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextInputLayout nameInput = binding.fullOrCompanyName;
        TextInputLayout birthdayInput = binding.birthday;
        TextInputLayout companyAddressInput = binding.companyAddress;
        TextInputLayout emailInput = binding.email;
        TextInputLayout confirmEmailInput = binding.confirmEmail;
        TextInputLayout passwordInput = binding.password;
        TextInputLayout confirmPasswordInput = binding.password;


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
            MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select birthday")
                    .build();

            picker.addOnPositiveButtonClickListener(selection -> {
                Date date = new Date(selection);
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                EditText birthdayEditText = birthdayInput.getEditText();
                if (birthdayEditText != null) {
                    birthdayEditText.setText(format.format(date));
                }
                Log.d("bday", "selection: " + selection);
            });

            picker.show(getSupportFragmentManager(), null);
        });

        binding.signupButton.setOnClickListener(v -> {
            String name = getTextFromTextInputLayout(nameInput);
//            String addressOrBirthday = addressOrBirthdayInput.getText().toString().trim();
            String addressOrBirthday;
            if (binding.jobToggle.isOn()) {
                addressOrBirthday = getTextFromTextInputLayout(companyAddressInput);
            } else {
                addressOrBirthday = getTextFromTextInputLayout(birthdayInput);
            }
            String email = getTextFromTextInputLayout(emailInput);
            String confirmEmail = getTextFromTextInputLayout(confirmEmailInput);
            String password = getTextFromTextInputLayout(passwordInput);
            String confirmPassword = getTextFromTextInputLayout(confirmPasswordInput);

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

            int userType = binding.jobToggle.isOn() ? 1 : 0;

//            Need to connect firebase here

            Toast.makeText(SignupActivity.this, userType + " signed up successfully", Toast.LENGTH_SHORT).show();
        });
    }

    private String getTextFromTextInputLayout(TextInputLayout textInputLayout) {
        EditText editText = textInputLayout.getEditText();
        if (editText != null) {
            return editText.getText().toString().trim();
        } else {
            return "";
        }
    }
}
