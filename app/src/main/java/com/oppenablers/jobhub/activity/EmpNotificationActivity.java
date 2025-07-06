package com.oppenablers.jobhub.activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.oppenablers.jobhub.R;

public class EmpNotificationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp_notifications_tab);

        ImageView returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(v -> finish());
    }
}
