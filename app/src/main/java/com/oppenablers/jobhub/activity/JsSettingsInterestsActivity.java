package com.oppenablers.jobhub.activity;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.api.JobHubClient;
import com.oppenablers.jobhub.databinding.ActivityJsSettingsInterestsBinding;
import com.oppenablers.jobhub.model.Interests;
import com.oppenablers.jobhub.model.JobSeeker;

public class JsSettingsInterestsActivity extends AppCompatActivity {

    ActivityJsSettingsInterestsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityJsSettingsInterestsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.topAppBar.setNavigationOnClickListener(v -> {
            finish();
        });

        binding.computerScienceGroup.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        binding.medicineGroup.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);

        Interests.getComputerScienceMap().forEach((integer, s) -> {
            addChip(binding.computerScienceGroup, integer, s);
        });

        Interests.getMedicineMap().forEach((integer, s) -> {
            addChip(binding.medicineGroup, integer, s);
        });

        JobHubClient.getAccountInfoJobSeeker(new JobHubClient.JobHubCallback<JobSeeker>() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(JobSeeker result) {
                result.getInterests().forEach(integer -> {
                    binding.computerScienceGroup.check(integer);
                    binding.medicineGroup.check(integer);
                });
            }
        });

        binding.confirmButton.setOnClickListener(v -> {

            binding.computerScienceGroup.getCheckedChipIds().forEach(integer -> {
                Log.d("Interests", "Checked chip: " + integer);
            });

            JobHubClient.getAccountInfoJobSeeker(new JobHubClient.JobHubCallback<JobSeeker>() {
                @Override
                public void onFailure() {

                }

                @Override
                public void onSuccess(JobSeeker result) {

                    result.getInterests().clear();

                    binding.computerScienceGroup.getCheckedChipIds().forEach(integer -> {
                        result.getInterests().add(integer);
                    });

                    binding.medicineGroup.getCheckedChipIds().forEach(integer -> {
                        result.getInterests().add(integer);
                    });

                    JobHubClient.updateAccountInfoJobSeeker(result, new JobHubClient.JobHubCallbackVoid() {
                        @Override
                        public void onFailure() {

                        }

                        @Override
                        public void onSuccess() {
                            finish();
                        }
                    });
                }
            });
        });
    }

    private void addChip(ChipGroup chipGroup, int key, String s) {
        Chip chip = (Chip) getLayoutInflater().inflate(R.layout.interests_chip, binding.computerScienceGroup, false);
        chip.setId(key);
        chip.setText(s);
        chipGroup.addView(chip);
    }
}