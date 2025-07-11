package com.oppenablers.jobhub.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.oppenablers.jobhub.R;

public class JsMessagesFragment extends Fragment {

    public JsMessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_js_messages, container, false);
        LinearLayout message1 = view.findViewById(R.id.message_item_1);

        message1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), com.oppenablers.jobhub.activity.JsDirectMessageActivity.class);
                intent.putExtra("userId", "1");
                intent.putExtra("userName", "Mutsuki");
                startActivity(intent);
            }
        });

        return view;
    }
}