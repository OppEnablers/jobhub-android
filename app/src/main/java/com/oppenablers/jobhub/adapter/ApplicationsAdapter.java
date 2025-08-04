package com.oppenablers.jobhub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.databinding.ItemVacancyBinding;
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

        ItemVacancyBinding binding;

        public ApplicationViewHolder(@NonNull View itemView, View.OnClickListener onClickListener) {
            super(itemView);
            binding = ItemVacancyBinding.bind(itemView);
            binding.vacancyContainer.setOnClickListener(onClickListener);
        }

        public void setItems(Job job) {

            binding.vacancyTitle.setText(job.getName());
            binding.vacancyDescription.setText(job.getCompanyName());
        }
    }
}
