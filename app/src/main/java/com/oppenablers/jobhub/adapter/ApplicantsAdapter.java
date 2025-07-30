package com.oppenablers.jobhub.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ApplicantsAdapter extends RecyclerView.Adapter<ApplicantsAdapter.ApplicantsViewHolder> {

    @NonNull
    @Override
    public ApplicantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicantsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ApplicantsViewHolder extends RecyclerView.ViewHolder {

        public ApplicantsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
