package com.oppenablers.jobhub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.Utility;
import com.oppenablers.jobhub.databinding.ItemJobBinding;
import com.oppenablers.jobhub.model.Job;
import com.oppenablers.jobhub.model.JobModality;
import com.oppenablers.jobhub.model.JobShift;
import com.oppenablers.jobhub.model.JobTime;

import java.util.List;
import java.util.Locale;

public class JobCardAdapter extends RecyclerView.Adapter<JobCardAdapter.JobCardViewHolder> {

    private final List<Job> jobs;

    private ImageButton toggleButton;
    private int swipeUpId;
    private int swipeDownId;

    public JobCardAdapter(List<Job> jobs) {
        this.jobs = jobs;
    }

    @NonNull
    @Override
    public JobCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JobCardViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_job, parent, false),
                toggleButton,
                swipeUpId,
                swipeDownId);
    }

    @Override
    public void onBindViewHolder(@NonNull JobCardViewHolder holder, int position) {
        Job job = jobs.get(position);
        holder.setItems(job);
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public Job getJob(int index) {
        if (index < 0 && jobs.isEmpty()) return null;
        return jobs.get(index);
    }

    public void setToggleButton(ImageButton toggleButton, int swipeUpId, int swipeDownId) {
        this.toggleButton = toggleButton;
        this.swipeUpId = swipeUpId;
        this.swipeDownId = swipeDownId;
    }

    public static class JobCardViewHolder extends RecyclerView.ViewHolder {

        ItemJobBinding binding;

        public JobCardViewHolder(
                @NonNull View itemView,
                ImageButton toggleButton,
                int swipeUpId,
                int swipeDownId) {
            super(itemView);

            binding = ItemJobBinding.bind(itemView);

            ScrollView scrollView = binding.detailsScrollView;
            SwipeRevealLayout srl = binding.swipeLayout;

            scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> srl.setLockDrag(scrollY > 0));

            srl.setSwipeListener(new SwipeRevealLayout.SwipeListener() {
                @Override
                public void onClosed(SwipeRevealLayout view) {
                    toggleButton.setImageResource(swipeUpId);
                }

                @Override
                public void onOpened(SwipeRevealLayout view) {
                    toggleButton.setImageResource(swipeDownId);
                }

                @Override
                public void onSlide(SwipeRevealLayout view, float slideOffset) {
                }
            });
        }

        public void setItems(Job job) {
            binding.jobPosition.setText(job.getName());
            binding.jobLocation.setText(job.getLocation());
            binding.companyName.setText(job.getCompanyName());
            // time, shift, modality

            String time = "";
            if (job.getTime() == JobTime.FULL_TIME) time = "Full-Time";
            else if (job.getTime() == JobTime.PART_TIME) time = "Part-TIme";

            int shiftFlags = job.getShift();
            String shift = "";
            if (Utility.hasFlag(shiftFlags, JobShift.DAY_SHIFT))
                shift += "Day Shift";
            if (!shift.isEmpty()) shift += ", ";
            if (Utility.hasFlag(shiftFlags, JobShift.NIGHT_SHIFT))
                shift += "Night Shift";

            int modalityFlags = job.getModality();
            String modality = "";
            if (Utility.hasFlag(modalityFlags, JobModality.ON_SITE))
                modality = "On-Site";
            if (Utility.hasFlag(modalityFlags, JobModality.WORK_FROM_HOME)) {
                if (modality.isEmpty()) {
                    modality = "Work from Home";
                } else {
                    modality = "Hybrid";
                }
            }

            String jobType = String.format(Locale.getDefault(),
                    "%s (%s), %s", time, shift, modality);
            binding.jobType.setText(jobType);
            binding.jobObjectives.setText(job.getObjectives());
            binding.jobSkills.setText(job.getSkills());

            String salaryRange = String.format(
                    Locale.getDefault(), "₱%,.2f - ₱%,.2f",
                    job.getMinimumSalary(), job.getMaximumSalary());
            binding.jobSalary.setText(salaryRange);
            binding.jobWorkExperience.setText(
                    itemView.getResources()
                            .getStringArray(R.array.items_work_experience)[job.getWorkExperience()]
            );

            binding.backgroundImage.setImageResource(R.drawable.sample_bg);
        }
    }
}
