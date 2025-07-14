package com.oppenablers.jobhub.fragment;

import android.animation.LayoutTransition;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.activity.EmpAddVacancyActivity;
import com.oppenablers.jobhub.adapter.VacancyAdapter;
import com.oppenablers.jobhub.api.JobHubClient;
import com.oppenablers.jobhub.databinding.FragmentEmpVacanciesBinding;
import com.oppenablers.jobhub.model.Vacancy;

import java.util.ArrayList;
import java.util.Comparator;

public class EmpVacanciesFragment extends Fragment {

    FragmentEmpVacanciesBinding binding;

    public EmpVacanciesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEmpVacanciesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonAddVacancy.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), EmpAddVacancyActivity.class));
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        JobHubClient.getVacanciesEmployer(new JobHubClient.JobHubCallback<ArrayList<Vacancy>>() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(ArrayList<Vacancy> result) {
                result.sort(Comparator.comparing(Vacancy::getName));
                VacancyAdapter vacancyAdapter = new VacancyAdapter(result);

                binding.vacancyList.setAdapter(vacancyAdapter);
                binding.vacancyList.setLayoutManager(
                        new LinearLayoutManager(getContext()));
            }
        });
    }
}