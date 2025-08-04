package com.oppenablers.jobhub.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oppenablers.jobhub.adapter.ApplicantsAdapter;
import com.oppenablers.jobhub.api.JobHubClient;
import com.oppenablers.jobhub.databinding.FragmentEmpApplicantsBinding;
import com.oppenablers.jobhub.model.ApplicantData;

import java.util.ArrayList;

public class EmpJsFragment extends Fragment {

    FragmentEmpApplicantsBinding binding;

    public EmpJsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmpApplicantsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.applicantList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();

        JobHubClient.getApplicantsEmployer(new JobHubClient.JobHubCallback<ArrayList<ApplicantData>>() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(ArrayList<ApplicantData> result) {
                ApplicantsAdapter adapter = new ApplicantsAdapter(result);
                adapter.setClickListener(new ApplicantsAdapter.ApplicantClickListener() {
                    @Override
                    public void onMessage(String userId, String vacancyId) {
                        JobHubClient.acceptApplicantEmployer(userId, vacancyId, new JobHubClient.JobHubCallbackVoid() {
                            @Override
                            public void onFailure() {

                            }

                            @Override
                            public void onSuccess() {

                            }
                        });
                    }

                    @Override
                    public void onDelete(String userId, String vacancyId) {

                    }
                });
                binding.applicantList.setAdapter(adapter);
            }
        });
    }
}