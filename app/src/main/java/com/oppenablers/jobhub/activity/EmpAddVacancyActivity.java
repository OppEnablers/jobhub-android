package com.oppenablers.jobhub.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.api.JobHubClient;
import com.oppenablers.jobhub.databinding.ActivityEmpAddVacancyBinding;
import com.oppenablers.jobhub.model.Vacancy;

import java.util.Arrays;

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

        binding.jobDetailsScrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            binding.swipeLayout.setLockDrag(scrollY > 0);
        });

        binding.confirmButton.setOnClickListener(v -> {

            String jobPosition = binding.jobPosition.getText().toString();
            String jobLocation = binding.jobLocation.getText().toString();
            int workExp = getWorkExperienceIndex(binding.jobWorkExperience.getText().toString());

            Vacancy vacancy = new Vacancy(
                    "",
                    jobPosition,
                    jobLocation
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

    private int getWorkExperienceIndex(String workExperience) {
        String[] workExperienceArray = getResources().getStringArray(R.array.items_work_experience);
        for (int i = 0; i < workExperienceArray.length; i++) {
            if (workExperienceArray[i].contentEquals(workExperience))
                return i;
        }
        return -1;
    }
}