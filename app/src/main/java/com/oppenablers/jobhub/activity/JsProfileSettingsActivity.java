package com.oppenablers.jobhub.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.databinding.ActivityJsProfileSettingsBinding;
import com.oppenablers.jobhub.fragment.JsProfileSettingEducationFragment;
import com.oppenablers.jobhub.fragment.JsProfileSettingExperienceFragment;
import com.oppenablers.jobhub.fragment.JsProfileSettingObjectivesFragment;
import com.oppenablers.jobhub.fragment.JsProfileSettingPersonalFragment;

public class JsProfileSettingsActivity extends AppCompatActivity {

    ActivityJsProfileSettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityJsProfileSettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.topAppBar.setNavigationOnClickListener(v -> finish());

        Intent intent = getIntent();
        if (intent == null) return;

        String title = intent.getStringExtra("title");
        if (title == null) return;

        binding.topAppBar.setTitle(title);

        String setting = intent.getStringExtra("profile_setting");
        if (setting == null) return;

        Fragment fragment = null;
        switch (setting) {
            case "personal":
                fragment = new JsProfileSettingPersonalFragment();
                break;
            case "objectives":
                fragment = new JsProfileSettingObjectivesFragment();
                break;
            case "education":
                fragment = new JsProfileSettingEducationFragment();
                break;
            case "experience":
                fragment = new JsProfileSettingExperienceFragment();
                break;
            case "skills":
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}