package com.oppenablers.jobhub.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.api.JobHubClient;
import com.oppenablers.jobhub.databinding.FragmentJsProfileSettingObjectivesBinding;
import com.oppenablers.jobhub.model.JobSeeker;


public class JsProfileSettingObjectivesFragment extends Fragment {

    FragmentJsProfileSettingObjectivesBinding binding;

    public JsProfileSettingObjectivesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentJsProfileSettingObjectivesBinding
                .inflate(inflater, container, false);
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
                EditText editText = binding.objectivesTextInput.getEditText();
                if (editText == null) return;

                editText.setText(result.getObjectives());

                binding.updateButton.setOnClickListener(v -> {

                    result.setObjectives(editText.getText().toString());
                    JobHubClient.updateAccountInfoJobSeeker(result, new JobHubClient.JobHubCallbackVoid() {
                        @Override
                        public void onFailure() {

                        }

                        @Override
                        public void onSuccess() {
                            FragmentActivity activity = getActivity();
                            if (activity != null) activity.finish();
                        }
                    });
                });
            }
        });
    }
}