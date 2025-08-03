package com.oppenablers.jobhub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.databinding.ItemJobseekerBinding;
import com.oppenablers.jobhub.model.JobSeeker;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ApplicantsViewHolder> {

    private final ArrayList<JobSeeker> applicants;
    private OnClickListener onClickListener;

    public MessagesAdapter(ArrayList<JobSeeker> applicants) {
        this.applicants = applicants;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public ApplicantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ApplicantsViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_jobseeker, parent, false),
                onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicantsViewHolder holder, int position) {
        JobSeeker jobSeeker = applicants.get(position);
        holder.setJobSeeker(jobSeeker, onClickListener);
    }

    @Override
    public int getItemCount() {
        return applicants.size();
    }

    public static class ApplicantsViewHolder extends RecyclerView.ViewHolder {

        ItemJobseekerBinding binding;

        public ApplicantsViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);
            binding = ItemJobseekerBinding.bind(itemView);
        }

        public void setJobSeeker(JobSeeker jobSeeker, OnClickListener onClickListener) {
            binding.vacancyTitle.setText(jobSeeker.getName());
            binding.vacancyContainer.setOnClickListener(v -> {
                onClickListener.onClick(jobSeeker.getUserId(), jobSeeker.getName());
            });
        }
    }

    public interface OnClickListener {
        void onClick(String userId, String userName);
    }
}
