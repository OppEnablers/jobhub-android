package com.oppenablers.jobhub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.model.Job;
import com.oppenablers.jobhub.model.Vacancy;

import java.util.List;

public class JobCardAdapter extends RecyclerView.Adapter<JobCardAdapter.JobCardViewHolder> {

    private final List<Vacancy> jobs;

    public JobCardAdapter(List<Vacancy> jobs) {
        this.jobs = jobs;
    }

    @NonNull
    @Override
    public JobCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new JobCardViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_job, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull JobCardViewHolder holder, int position) {
        Vacancy job = jobs.get(position);
        holder.setItems(job);
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public Vacancy getJob(int index) {
        return jobs.get(index);
    }

    public static class JobCardViewHolder extends RecyclerView.ViewHolder {

        private final TextView jobPosition;
        private final TextView companyName;
        private final ImageView backgroundImage;

        public JobCardViewHolder(@NonNull View itemView) {
            super(itemView);

            jobPosition = itemView.findViewById(R.id.job_position);
            companyName = itemView.findViewById(R.id.job_location);
            backgroundImage = itemView.findViewById(R.id.backgroundImage);
        }

        public void setItems(Vacancy job) {
            jobPosition.setText(job.getName());
            companyName.setText(job.getLocation());
            backgroundImage.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }
}
