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
import android.widget.LinearLayout;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.activity.EmpMessagesDirectActivity;
import com.oppenablers.jobhub.adapter.MessagesAdapter;
import com.oppenablers.jobhub.api.JobHubClient;
import com.oppenablers.jobhub.databinding.FragmentEmpMessagesBinding;
import com.oppenablers.jobhub.model.JobSeeker;

import java.util.ArrayList;

public class EmpMessagesFragment extends Fragment {

    FragmentEmpMessagesBinding binding;

    public EmpMessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmpMessagesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        View view = inflater.inflate(R.layout.fragment_emp_messages, container, false);
//        LinearLayout message1 = view.findViewById(R.id.message_card_1);
//
//        message1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), EmpMessagesDirectActivity.class);
//                intent.putExtra("userId", "xCOv7zBFK0eYoKZZ2gdXEHSUzN83");
//                intent.putExtra("userName", "Mutsuki");
//                startActivity(intent);
//            }
//        });
//
//        return view;
        binding.messagesList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onResume() {
        super.onResume();

        JobHubClient.getChatsEmployer(new JobHubClient.JobHubCallback<ArrayList<JobSeeker>>() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(ArrayList<JobSeeker> result) {
                MessagesAdapter adapter = new MessagesAdapter(result);
                adapter.setOnClickListener(new MessagesAdapter.OnClickListener() {
                    @Override
                    public void onClick(String userId, String userName) {
                        Intent intent = new Intent(getActivity(), EmpMessagesDirectActivity.class);
                        intent.putExtra(EmpMessagesDirectActivity.EXTRA_USER_ID, userId);
                        intent.putExtra(EmpMessagesDirectActivity.EXTRA_USER_NAME, userName);
                        startActivity(intent);
                    }
                });
                binding.messagesList.setAdapter(adapter);
            }
        });
    }
}