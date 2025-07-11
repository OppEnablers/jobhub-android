package com.oppenablers.jobhub.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.oppenablers.jobhub.activity.JsProfileSettingsActivity;
import com.oppenablers.jobhub.api.JobHubClient;
import com.oppenablers.jobhub.databinding.FragmentJsProfileBinding;
import com.oppenablers.jobhub.model.JobSeeker;

public class JsProfileFragment extends Fragment {

    FragmentJsProfileBinding binding;

    public JsProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = FragmentJsProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.objectives.profileItemName.setText("Objectives");
        binding.education.profileItemName.setText("Education");
        binding.experience.profileItemName.setText("Work Experience");
        binding.skills.profileItemName.setText("Skills");

        binding.objectives.getRoot().setOnClickListener(v -> {
            startActivity(createSettingIntent("Objectives", "objectives"));
        });

        binding.education.getRoot().setOnClickListener(v -> {
            startActivity(createSettingIntent("Education", "education"));
        });

        binding.experience.getRoot().setOnClickListener(v -> {
            startActivity(createSettingIntent("Work Experience", "experience"));
        });

        binding.skills.getRoot().setOnClickListener(v -> {
            startActivity(createSettingIntent("Skills", "skills"));
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        updateInfo();
    }

    private Intent createSettingIntent(String title, String settingName) {
        Intent intent = new Intent(requireContext(), JsProfileSettingsActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("profile_setting", settingName);
        return intent;
    }

    private void updateInfo() {
        JobHubClient.getAccountInfoJobSeeker(new JobHubClient.JobHubCallback<JobSeeker>() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(JobSeeker result) {
                binding.profileName.setText(result.getName());
                binding.profileName.setOnClickListener(v -> {
                    startActivity(createSettingIntent("Personal Info", "personal"));
                });
                binding.objectives.profileItemValue.setText(result.getObjectives());
                binding.objectives.getRoot().setOnClickListener(v -> {
                    startActivity(createSettingIntent("Objectives", "objectives"));
                });
                binding.education.profileItemValue.setText(result.getEducation());
                binding.education.getRoot().setOnClickListener(v -> {
                    startActivity(createSettingIntent("Education", "education"));
                });
                binding.experience.profileItemValue.setText(result.getWorkExperience());
                binding.experience.getRoot().setOnClickListener(v -> {
                    startActivity(createSettingIntent("Work Experience", "experience"));
                });
                binding.skills.profileItemValue.setText("");
                binding.skills.getRoot().setOnClickListener(v -> {
                    startActivity(createSettingIntent("Skills", "skills"));
                });
            }
        });
    }
}

