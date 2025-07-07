package com.oppenablers.jobhub.activity;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.oppenablers.jobhub.AuthManager;
import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.databinding.ActivityJsSettingBinding;
import com.oppenablers.jobhub.fragment.JsSettingsFragment;

public class JsSettingsActivity extends AppCompatActivity {

    ActivityJsSettingBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityJsSettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        binding.emailLayout.setOnClickListener(v -> {
//            Intent intent = new Intent(this, JsSettingsEmail.class);
//            startActivity(intent);
//        });
//
//        binding.phoneNumberLayout.setOnClickListener(v -> {
//            Intent intent = new Intent(this, JsSettingsPhoneNum.class);
//            startActivity(intent);
//        });
//
//        binding.interestsLayout.setOnClickListener(v -> {
//            Intent intent = new Intent(this, JsSettingsInterests.class);
//            startActivity(intent);
//        });
//
//        binding.locationLayout.setOnClickListener(v -> {
//            Intent intent = new Intent(this, JsSettingsLocation.class);
//            startActivity(intent);
//        });

        binding.content.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
//
        binding.logoutButton.setOnClickListener(v -> {
            AuthManager.logout();

            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new JsSettingsFragment())
                .commit();

        binding.topAppBar.setNavigationOnClickListener(v -> {
            finish();
        });
    }
}