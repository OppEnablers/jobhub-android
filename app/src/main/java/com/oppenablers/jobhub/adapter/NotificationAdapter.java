package com.oppenablers.jobhub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.model.Notification;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private final List<Notification> notifications;

    public NotificationAdapter(List<Notification> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotificationViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.setSource(notification.getSource());
        holder.setMessage(notification.getMessage());
        holder.setTime("Unknown");
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        private final TextView source;
        private final TextView message;
        private final TextView time;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            source = (TextView)itemView.findViewById(R.id.notification_source);
            message = (TextView)itemView.findViewById(R.id.notification_message);
            time = (TextView)itemView.findViewById(R.id.notification_time);
        }

        public void setSource(String text) {
            source.setText(text);
        }

        public void setMessage(String text) {
            message.setText(text);
        }

        public void setTime(String text) {
            time.setText(text);
        }
    }
}
