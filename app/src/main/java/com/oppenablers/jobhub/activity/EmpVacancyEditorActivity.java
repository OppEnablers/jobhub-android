package com.oppenablers.jobhub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.Utility;
import com.oppenablers.jobhub.api.JobHubClient;
import com.oppenablers.jobhub.databinding.ActivityEmpVacancyEditorBinding;
import com.oppenablers.jobhub.model.JobModality;
import com.oppenablers.jobhub.model.JobShift;
import com.oppenablers.jobhub.model.Vacancy;
import com.oppenablers.jobhub.model.JobTime;

import java.util.Locale;

public class EmpVacancyEditorActivity extends AppCompatActivity {

    public static final String EXTRA_MODE = "EmpVacancyEditor.MODE";
    public static final String EXTRA_VACANCY_ID = "EmpVacancyEditor.VACANCY_ID";
    public static final int MODE_ADD = 0;
    public static final int MODE_EDIT = 1;

    private ActivityEmpVacancyEditorBinding binding;

    private int mode;
    private String vacancyId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityEmpVacancyEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mode = MODE_ADD;
        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_MODE) && intent.hasExtra(EXTRA_VACANCY_ID)) {
            mode = intent.getIntExtra(EXTRA_MODE, MODE_ADD);
            vacancyId = intent.getStringExtra(EXTRA_VACANCY_ID);
        } else {
            throw new RuntimeException("Incomplete intent.");
        }

        if (mode == MODE_ADD) {
            binding.topAppBar.setTitle("Add Activity");
        } else if (mode == MODE_EDIT) {
            binding.topAppBar.setTitle("Edit Activity");
        }

        JobHubClient.getVacancy(vacancyId, new JobHubClient.JobHubCallback<Vacancy>() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(Vacancy result) {
                loadVacancy(result);
            }
        });

        binding.topAppBar.setNavigationOnClickListener(v -> {
            finish();
        });

        binding.jobDetailsScrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            binding.swipeLayout.setLockDrag(scrollY > 0);
        });

        binding.confirmButton.setOnClickListener(v -> {

            String position = binding.jobPosition.getText().toString();
            String location = binding.jobLocation.getText().toString();
            int time = getTime();
            int shift = getShift();
            int modality = getModality();
            String objectives = binding.jobObjectives.getText().toString();
            String skills = binding.jobSkills.getText().toString();
            float minSalary = 0f;
            float maxSalary = 0f;
            try {
                minSalary = Float.parseFloat(binding.jobMinimumPay.getText().toString());
                maxSalary = Float.parseFloat(binding.jobMaximumPay.getText().toString());
            } catch (NumberFormatException ignored) {
            }
            int workExp = getWorkExperience();

            if (position.isEmpty() ||
                    location.isEmpty() ||
                    time < 0 ||
                    shift < 0 ||
                    objectives.isEmpty() ||
                    skills.isEmpty() ||
                    minSalary < 1200 ||
                    maxSalary < minSalary ||
                    workExp < 0) {
                if (minSalary < 1200) {
                    Toast.makeText(this, "Minimum salary cannot be below Php 1200.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            Vacancy vacancy = new Vacancy(
                    vacancyId,
                    position,
                    location,
                    time,
                    shift,
                    modality,
                    objectives,
                    skills,
                    minSalary,
                    maxSalary,
                    workExp
            );

            if (mode == MODE_ADD) {
                JobHubClient.addVacancy(vacancy, new JobHubClient.JobHubCallbackVoid() {
                    @Override
                    public void onFailure() {

                    }

                    @Override
                    public void onSuccess() {
                        finish();
                    }
                });
            } else {
                JobHubClient.updateVacancy(vacancy, new JobHubClient.JobHubCallbackVoid() {
                    @Override
                    public void onFailure() {

                    }

                    @Override
                    public void onSuccess() {
                        finish();
                    }
                });
            }
        });
    }

    private void loadVacancy(Vacancy vacancy) {
        binding.jobPosition.setText(vacancy.getName());
        binding.jobLocation.setText(vacancy.getLocation());
        setTime(vacancy.getTime());
        setShift(vacancy.getShift());
        setModality(vacancy.getModality());
        binding.jobObjectives.setText(vacancy.getObjectives());
        binding.jobSkills.setText(vacancy.getSkills());
        binding.jobMinimumPay.setText(
                String.format(Locale.getDefault(), "%.2f", vacancy.getMinimumSalary()));
        binding.jobMaximumPay.setText(
                String.format(Locale.getDefault(), "%.2f", vacancy.getMaximumSalary()));
        setWorkExperience(vacancy.getWorkExperience());
    }

    private int getTime() {
        if (binding.jobFullTime.isChecked()) return JobTime.FULL_TIME;
        else if (binding.jobPartTime.isChecked()) return JobTime.PART_TIME;
        else return -1;
    }

    private void setTime(int time) {
        if (time == JobTime.FULL_TIME) {
            binding.jobFullTime.setChecked(true);
        } else if (time == JobTime.PART_TIME) {
            binding.jobPartTime.setChecked(true);
        }
    }

    private int getShift() {
        int flags = 0;
        if (binding.jobDayShift.isChecked()) flags |= JobShift.DAY_SHIFT;
        if (binding.jobNightShift.isChecked()) flags |= JobShift.NIGHT_SHIFT;
        if (flags == 0) flags = -1;
        return flags;
    }

    private void setShift(int shift) {
        if (Utility.hasFlag(shift, JobShift.DAY_SHIFT)) binding.jobDayShift.setChecked(true);
        if (Utility.hasFlag(shift, JobShift.NIGHT_SHIFT)) binding.jobNightShift.setChecked(true);
    }

    private int getModality() {
        int flags = 0;
        if (binding.jobOnSite.isChecked()) flags |= JobModality.ON_SITE;
        if (binding.jobWorkFromHome.isChecked()) flags |= JobModality.WORK_FROM_HOME;
        if (flags == 0) flags = -1;
        return flags;
    }

    private void setModality(int modality) {
        if (Utility.hasFlag(modality, JobModality.ON_SITE)) binding.jobOnSite.setChecked(true);
        if (Utility.hasFlag(modality, JobModality.WORK_FROM_HOME)) binding.jobWorkFromHome.setChecked(true);
    }

    private int getWorkExperience() {
        String workExperience = binding.jobWorkExperience.getText().toString();
        String[] workExperienceArray = getResources().getStringArray(R.array.items_work_experience);
        for (int i = 0; i < workExperienceArray.length; i++) {
            if (workExperienceArray[i].contentEquals(workExperience))
                return i;
        }
        return -1;
    }

    private void setWorkExperience(int workExperience) {
        String[] workExperienceArray = getResources().getStringArray(R.array.items_work_experience);
        String workExperienceString = workExperienceArray[workExperience];
        binding.jobWorkExperience.setText(workExperienceString, false);
    }
}