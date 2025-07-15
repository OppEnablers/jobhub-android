package com.oppenablers.jobhub.adapter;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.model.Job;
import com.oppenablers.jobhub.model.Vacancy;

import java.util.List;

public class JobCardAdapter extends RecyclerView.Adapter<JobCardAdapter.JobCardViewHolder> {

    private final List<Vacancy> jobs;

    private ImageButton toggleButton;
    private int swipeUpId;
    private int swipeDownId;

    public JobCardAdapter(List<Vacancy> jobs) {
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
        Vacancy job = jobs.get(position);
        holder.setItems(job);
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public Vacancy getJob(int index) {
        if (index < 0 && jobs.isEmpty()) return null;
        return jobs.get(index);
    }

    public void setToggleButton(ImageButton toggleButton, int swipeUpId, int swipeDownId) {
        this.toggleButton = toggleButton;
        this.swipeUpId = swipeUpId;
        this.swipeDownId = swipeDownId;
    }

    public static class JobCardViewHolder extends RecyclerView.ViewHolder {

        private final TextView jobPosition;
        private final TextView companyName;
        private final ImageView backgroundImage;

        public JobCardViewHolder(
                @NonNull View itemView,
                ImageButton toggleButton,
                int swipeUpId,
                int swipeDownId) {
            super(itemView);

            SwipeRevealLayout srl = itemView.findViewById(R.id.swipe_layout);
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
                public void onSlide(SwipeRevealLayout view, float slideOffset) {}
            });

            jobPosition = itemView.findViewById(R.id.job_position);
            companyName = itemView.findViewById(R.id.job_location);
            backgroundImage = itemView.findViewById(R.id.backgroundImage);
        }

        public void setItems(Vacancy job) {
            jobPosition.setText(job.getName());
            companyName.setText(job.getLocation());
            backgroundImage.setImageResource(R.drawable.sample_bg);
        }
    }
}
