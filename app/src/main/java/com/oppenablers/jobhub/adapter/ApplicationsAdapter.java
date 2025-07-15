package com.oppenablers.jobhub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.model.Job;

import java.util.List;

public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ApplicationViewHolder> {

    private final List<Job> jobs;
    private View.OnClickListener onClickListener;

    public ApplicationsAdapter(List<Job> jobs) {
        this.jobs = jobs;
    }

    @NonNull
    @Override
    public ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ApplicationViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vacancy, parent, false),
                onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationViewHolder holder, int position) {
        Job job = jobs.get(position);
        holder.setItems(job);
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static class ApplicationViewHolder extends RecyclerView.ViewHolder {

        private final TextView jobPosition;
        private final TextView companyName;

        public ApplicationViewHolder(@NonNull View itemView, View.OnClickListener onClickListener) {
            super(itemView);

            itemView.findViewById(R.id.vacancy_container)
                    .setOnClickListener(onClickListener);
            jobPosition = itemView.findViewById(R.id.vacancy_title);
            companyName = itemView.findViewById(R.id.vacancy_description);
        }

        public void setItems(Job job) {
            jobPosition.setText(job.getName());
            companyName.setText(job.getCompanyName());
        }
    }
}
