package com.oppenablers.jobhub.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.oppenablers.jobhub.activity.EmpVacancyEditorActivity;
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
            startActivity(new Intent(requireContext(), EmpVacancyEditorActivity.class));
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
                ArrayList<Vacancy> vacancies = result;
                VacancyAdapter vacancyAdapter = new VacancyAdapter(vacancies);
                vacancyAdapter.setOnClickListener(new VacancyAdapter.OnClickListener() {
                    @Override
                    public void onClick(String vacancyId) {
                        Intent intent = new Intent(getContext(), EmpVacancyEditorActivity.class);
                        intent.putExtra(
                                EmpVacancyEditorActivity.EXTRA_MODE,
                                EmpVacancyEditorActivity.MODE_EDIT);
                        intent.putExtra(
                                EmpVacancyEditorActivity.EXTRA_VACANCY_ID,
                                vacancyId);
                        startActivity(intent);
                    }

                    @Override
                    public void onDeleteClick(String vacancyId, int position) {
                        JobHubClient.deleteVacancy(vacancyId, new JobHubClient.JobHubCallback<Vacancy>() {
                            @Override
                            public void onFailure() {

                            }

                            @Override
                            public void onSuccess(Vacancy result) {
                                vacancies.remove(position);
                                vacancyAdapter.notifyItemRemoved(position);
                            }
                        });
                    }
                });

                binding.vacancyList.setAdapter(vacancyAdapter);
                binding.vacancyList.setLayoutManager(
                        new LinearLayoutManager(getContext()));
            }
        });
    }
}