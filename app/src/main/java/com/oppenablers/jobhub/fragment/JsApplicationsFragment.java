package com.oppenablers.jobhub.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.adapter.ApplicationsAdapter;
import com.oppenablers.jobhub.api.JobHubClient;
import com.oppenablers.jobhub.databinding.FragmentJsApplicationsBinding;
import com.oppenablers.jobhub.model.Job;

import java.util.ArrayList;

public class JsApplicationsFragment extends Fragment {

    FragmentJsApplicationsBinding binding;
    ApplicationsAdapter adapter;

    public JsApplicationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentJsApplicationsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.applicationList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();

        JobHubClient.getApplicationsJobSeeker(new JobHubClient.JobHubCallback<ArrayList<Job>>() {
            @Override
            public void onFailure() {
            }

            @Override
            public void onSuccess(ArrayList<Job> result) {
                adapter = new ApplicationsAdapter(result);
                binding.applicationList.setAdapter(adapter);
            }
        });
    }
}