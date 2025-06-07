package com.oppenablers.jobhub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.internal.EdgeToEdgeUtils;
import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.Util;
import com.oppenablers.jobhub.databinding.ActivityJsNavigatorBinding;
import com.oppenablers.jobhub.fragment.JsApplicationFragment;
import com.oppenablers.jobhub.fragment.JsJobFragment;
import com.oppenablers.jobhub.fragment.JsMessagesFragment;
import com.oppenablers.jobhub.fragment.JsProfileFragment;

public class JsNavigatorActivity extends AppCompatActivity {

    ActivityJsNavigatorBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJsNavigatorBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        replaceFragment(new JsJobFragment(), "jobs");

        binding.navJobs.setOnClickListener(v -> replaceFragment(new JsJobFragment(), "jobs"));
        binding.navStar.setOnClickListener(v -> replaceFragment(new JsApplicationFragment(), "application"));
        binding.navMessage.setOnClickListener(v -> replaceFragment(new JsMessagesFragment(), "message"));
        binding.navProfile.setOnClickListener(v -> replaceFragment(new JsProfileFragment(), "profile"));

        binding.topAppBar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.notifications) {
                Intent intent = new Intent(this, JsNotificationActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.settings) {
                Intent intent = new Intent(this, JsSettingsTabActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.logout_button) {

            } else {
                return false;
            }

            return true;
        });

//        ImageView bellIcon = findViewById(R.id.notification);
//        bellIcon.setOnClickListener(v -> {
//            Intent intent = new Intent(JsNavigatorActivity.this, JsNotificationActivity.class);
//            startActivity(intent);
//        });


    }

    private void replaceFragment(Fragment fragment, String selected) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

        updateNavIcons(selected);
    }

    private void updateNavIcons(String selected) {
        ImageView navJobs = findViewById(R.id.nav_jobs);
        ImageView navApplication = findViewById(R.id.nav_star);
        ImageView navMessage = findViewById(R.id.nav_message);
        ImageView navProfile = findViewById(R.id.nav_profile);

        navJobs.setImageResource(selected.equals("jobs") ? R.drawable.briefcase : R.drawable.briefcase);
        navApplication.setImageResource(selected.equals("application") ? R.drawable.star_blue : R.drawable.star);
        navMessage.setImageResource(selected.equals("message") ? R.drawable.message : R.drawable.message);
        navProfile.setImageResource(selected.equals("profile") ? R.drawable.person_blue : R.drawable.person);
    }
}
