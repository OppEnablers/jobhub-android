package com.oppenablers.jobhub.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.activity.JsProfileEducAttainment;
import com.oppenablers.jobhub.activity.JsProfileObjectives;
import com.oppenablers.jobhub.activity.JsProfilePersonalInfo;
import com.oppenablers.jobhub.activity.JsProfileSkills;
import com.oppenablers.jobhub.activity.JsProfileWorkExp;

public class JsProfileFragment extends Fragment {

    public JsProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_js_profile, container, false);

        View personalInfoInclude = view.findViewById(R.id.section_personal_info);
        Button btnEditPersonalInfo = personalInfoInclude.findViewById(R.id.btn_edit_section);
        TextView txtPersonalInfo = personalInfoInclude.findViewById(R.id.section_title);
        txtPersonalInfo.setText("Personal Information");
        btnEditPersonalInfo.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), JsProfilePersonalInfo.class))
        );

        View objectivesInclude = view.findViewById(R.id.section_objectives);
        Button btnEditObjectives = objectivesInclude.findViewById(R.id.btn_edit_section);
        TextView txtObjectives = objectivesInclude.findViewById(R.id.section_title);
        txtObjectives.setText("Objectives");
        btnEditObjectives.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), JsProfileObjectives.class))
        );

        View educationInclude = view.findViewById(R.id.section_education);
        Button btnEditEducation = educationInclude.findViewById(R.id.btn_edit_section);
        TextView txtEducation = educationInclude.findViewById(R.id.section_title);
        txtEducation.setText("Educational Attainment");
        btnEditEducation.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), JsProfileEducAttainment.class))
        );

        View experienceInclude = view.findViewById(R.id.section_experience);
        Button btnEditExperience = experienceInclude.findViewById(R.id.btn_edit_section);
        TextView txtExperience = experienceInclude.findViewById(R.id.section_title);
        txtExperience.setText("Work Experience");
        btnEditExperience.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), JsProfileWorkExp.class))
        );

        View skillsInclude = view.findViewById(R.id.section_skills);
        Button btnEditSkills = skillsInclude.findViewById(R.id.btn_edit_section);
        TextView txtSkills = skillsInclude.findViewById(R.id.section_title);
        txtSkills.setText("Skills");
        btnEditSkills.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), JsProfileSkills.class))
        );

        return view;
    }

}

