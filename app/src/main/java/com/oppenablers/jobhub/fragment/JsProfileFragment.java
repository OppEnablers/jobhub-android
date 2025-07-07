package com.oppenablers.jobhub.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.activity.JsProfileEducAttainment;
import com.oppenablers.jobhub.activity.JsProfileObjectives;
import com.oppenablers.jobhub.activity.JsProfilePersonalInfo;
import com.oppenablers.jobhub.activity.JsProfileSkills;
import com.oppenablers.jobhub.activity.JsProfileWorkExp;
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

        JobHubClient.getAccountInfoJobSeeker(new JobHubClient.JobHubCallback<JobSeeker>() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(JobSeeker result) {
                binding.profileName.setText(result.getName());

                binding.profileName.setOnClickListener(v -> {
                    startActivity(new Intent(requireContext(), JsProfilePersonalInfo.class));
                });
            }
        });


//        binding.personalInfo.profileItemName.setText("Personal Info");
        binding.objectives.profileItemName.setText("Objectives");
        binding.objectives.getRoot().setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), JsProfileObjectives.class));
        });

        binding.education.profileItemName.setText("Education");
        binding.education.getRoot().setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), JsProfileEducAttainment.class));
        });

        binding.experience.profileItemName.setText("Experience");
        binding.experience.getRoot().setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), JsProfileWorkExp.class));
        });

        binding.skills.profileItemName.setText("Skills");
        binding.skills.getRoot().setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), JsProfileSkills.class));
        });

//        View personalInfoInclude = view.findViewById(R.id.section_personal_info);
//        Button btnEditPersonalInfo = personalInfoInclude.findViewById(R.id.btn_edit_section);
//        TextView txtPersonalInfo = personalInfoInclude.findViewById(R.id.section_title);
//        txtPersonalInfo.setText("Personal Information");
//        btnEditPersonalInfo.setOnClickListener(v ->
//                startActivity(new Intent(requireContext(), JsProfilePersonalInfo.class))
//        );
//
//        View objectivesInclude = view.findViewById(R.id.section_objectives);
//        Button btnEditObjectives = objectivesInclude.findViewById(R.id.btn_edit_section);
//        TextView txtObjectives = objectivesInclude.findViewById(R.id.section_title);
//        txtObjectives.setText("Objectives");
//        btnEditObjectives.setOnClickListener(v ->
//                startActivity(new Intent(requireContext(), JsProfileObjectives.class))
//        );
//
//        View educationInclude = view.findViewById(R.id.section_education);
//        Button btnEditEducation = educationInclude.findViewById(R.id.btn_edit_section);
//        TextView txtEducation = educationInclude.findViewById(R.id.section_title);
//        txtEducation.setText("Educational Attainment");
//        btnEditEducation.setOnClickListener(v ->
//                startActivity(new Intent(requireContext(), JsProfileEducAttainment.class))
//        );
//
//        View experienceInclude = view.findViewById(R.id.section_experience);
//        Button btnEditExperience = experienceInclude.findViewById(R.id.btn_edit_section);
//        TextView txtExperience = experienceInclude.findViewById(R.id.section_title);
//        txtExperience.setText("Work Experience");
//        btnEditExperience.setOnClickListener(v ->
//                startActivity(new Intent(requireContext(), JsProfileWorkExp.class))
//        );
//
//        View skillsInclude = view.findViewById(R.id.section_skills);
//        Button btnEditSkills = skillsInclude.findViewById(R.id.btn_edit_section);
//        TextView txtSkills = skillsInclude.findViewById(R.id.section_title);
//        txtSkills.setText("Skills");
//        btnEditSkills.setOnClickListener(v ->
//                startActivity(new Intent(requireContext(), JsProfileSkills.class))
//        );
    }
}

