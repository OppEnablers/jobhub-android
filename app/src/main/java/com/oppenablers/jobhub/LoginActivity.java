package com.oppenablers.jobhub;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.oppenablers.mariatoggle.widget.LabeledSwitch;

public class LoginActivity extends AppCompatActivity {

    private LabeledSwitch jobToggle;
    private TextView roleLabel;
    private LinearLayout jobSeekerLayout, employerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        jobToggle = findViewById(R.id.jobToggle);
        jobSeekerLayout = findViewById(R.id.jobSeekerLayout);
        employerLayout = findViewById(R.id.employerLayout);

        jobToggle.setOnToggleListener((buttonView, isChecked) -> {
            if (isChecked) {
                employerLayout.setVisibility(View.VISIBLE);
                jobSeekerLayout.setVisibility(View.GONE);
            } else {
                employerLayout.setVisibility(View.GONE);
                jobSeekerLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}
