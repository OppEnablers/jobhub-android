package com.oppenablers.jobhub;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private Switch jobToggle;
    private TextView roleLabel;
    private LinearLayout jobSeekerLayout, employerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        jobToggle = findViewById(R.id.jobToggle);
        roleLabel = findViewById(R.id.roleLabel);
        jobSeekerLayout = findViewById(R.id.jobSeekerLayout);
        employerLayout = findViewById(R.id.employerLayout);

        jobToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                roleLabel.setText("Employer");
                employerLayout.setVisibility(View.VISIBLE);
                jobSeekerLayout.setVisibility(View.GONE);
            } else {
                roleLabel.setText("Job Seeker");
                employerLayout.setVisibility(View.GONE);
                jobSeekerLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}

