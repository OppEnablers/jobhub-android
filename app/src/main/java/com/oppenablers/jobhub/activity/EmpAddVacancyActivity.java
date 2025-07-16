package com.oppenablers.jobhub.activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.api.JobHubClient;
import com.oppenablers.jobhub.databinding.ActivityEmpAddVacancyBinding;
import com.oppenablers.jobhub.model.Vacancy;

public class EmpAddVacancyActivity extends AppCompatActivity {

    ActivityEmpAddVacancyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityEmpAddVacancyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.topAppBar.setNavigationOnClickListener(v -> {
            finish();
        });

        binding.confirmButton.setOnClickListener(v -> {
            Vacancy vacancy = new Vacancy(
                    "",
                    binding.jobPosition.getText().toString(),
                    binding.jobLocation.getText().toString()
            );
            JobHubClient.addVacancy(vacancy, new JobHubClient.JobHubCallbackVoid() {
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
}