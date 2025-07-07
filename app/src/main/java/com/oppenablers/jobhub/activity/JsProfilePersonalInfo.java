package com.oppenablers.jobhub.activity;

import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.api.JobHubClient;
import com.oppenablers.jobhub.databinding.ActivityJsProfilePersonalInfoBinding;
import com.oppenablers.jobhub.model.JobSeeker;

public class JsProfilePersonalInfo extends AppCompatActivity {

    ActivityJsProfilePersonalInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJsProfilePersonalInfoBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.topAppBar.setNavigationOnClickListener(v -> finish());

        JobHubClient.getAccountInfoJobSeeker(new JobHubClient.JobHubCallback<JobSeeker>() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(JobSeeker result) {

                EditText editText = binding.fullNameTextInput.getEditText();
                if (editText == null) return;

                editText.setText(result.getName());

                binding.updateButton.setOnClickListener(v -> {

                    result.setName(editText.getText().toString());
                    JobHubClient.updateAccountInfoJobSeeker(result, new JobHubClient.JobHubCallbackVoid() {
                        @Override
                        public void onFailure() {

                        }

                        @Override
                        public void onSuccess() {
                            finish();
                        }
                    });
                });
            }
        });
    }
}
