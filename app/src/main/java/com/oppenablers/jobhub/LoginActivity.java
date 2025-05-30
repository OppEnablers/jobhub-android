package com.oppenablers.jobhub;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.oppenablers.jobhub.databinding.ActivityLoginBinding;
import com.oppenablers.mariatoggle.widget.LabeledSwitch;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.jobToggle.setOnToggleListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.employerLayout.setVisibility(View.VISIBLE);
                binding.jobSeekerLayout.setVisibility(View.GONE);
            } else {
                binding.employerLayout.setVisibility(View.GONE);
                binding.jobSeekerLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}
