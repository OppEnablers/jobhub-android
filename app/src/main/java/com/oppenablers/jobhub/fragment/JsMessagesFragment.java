package com.oppenablers.jobhub.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.activity.JsMessagesDirectActivity;
import com.oppenablers.jobhub.databinding.FragmentJsMessagesBinding;

public class JsMessagesFragment extends Fragment {

    FragmentJsMessagesBinding binding;

    public JsMessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentJsMessagesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        LinearLayout message1 = ;
//
//        message1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), JsMessagesDirectActivity.class);
//                intent.putExtra("userId", "yhPaESRBdCct8vphE4b7de0paGo1");
//                intent.putExtra("userName", "iACADEMY");
//                startActivity(intent);
//            }
//        });
    }
}