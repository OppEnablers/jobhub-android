package com.oppenablers.jobhub.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oppenablers.jobhub.adapter.NotificationAdapter;
import com.oppenablers.jobhub.databinding.ActivityEmpNotificationsBinding;
import com.oppenablers.jobhub.model.Notification;

import java.util.ArrayList;
import java.util.Date;

public class EmpNotificationsActivity extends AppCompatActivity {

    ActivityEmpNotificationsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityEmpNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.topAppBar.setNavigationOnClickListener(v -> finish());

        ArrayList<Notification> notifications = new ArrayList<>();
        notifications.add(new Notification(
                "JobHub",
                "You have a new application.",
                new Date()));
        notifications.add(new Notification(
                "JobHub",
                "You have 2 new applications.",
                new Date()));
        notifications.add(new Notification(
                "Nash Jerby Joo",
                "Thank you! I will send my resume right away.",
                new Date()));

        NotificationAdapter adapter = new NotificationAdapter(notifications);

        binding.notificationList.setLayoutManager(new LinearLayoutManager(this));
        binding.notificationList.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAbsoluteAdapterPosition();

                Log.d("Notifications", "Swiped " + position);

                notifications.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });
        itemTouchHelper.attachToRecyclerView(binding.notificationList);
    }
}