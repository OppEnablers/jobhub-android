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
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.statusBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        replaceFragment(new JsJobFragment(), "jobs");

        binding.bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.jobs) {
                replaceFragment(new JsJobFragment(), "jobs");
            } else if (itemId == R.id.applications) {
                replaceFragment(new JsApplicationFragment(), "application");
            } else if (itemId == R.id.messages) {
                replaceFragment(new JsMessagesFragment(), "message");
            } else if (itemId == R.id.profile) {
                replaceFragment(new JsProfileFragment(), "profile");
            } else {
                return false;
            }

            return true;
        });

        binding.topAppBar.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.notifications) {
                Intent intent = new Intent(this, JsNotificationActivity.class);
                startActivity(intent);
            } else if (itemId == R.id.settings) {
                Intent intent = new Intent(this, JsSettingsActivity.class);
                startActivity(intent);
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
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
