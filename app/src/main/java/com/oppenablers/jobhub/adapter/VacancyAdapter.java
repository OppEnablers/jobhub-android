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

public class VacancyAdapter extends RecyclerView.Adapter<VacancyAdapter.VacancyViewHolder> {

    private final List<Vacancy> vacancies;
    private View.OnClickListener onClickListener;

    public VacancyAdapter(List<Vacancy> jobs) {
        this.vacancies = jobs;
    }

    @NonNull
    @Override
    public VacancyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VacancyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vacancy, parent, false),
                onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull VacancyViewHolder holder, int position) {
        Vacancy vacancy = vacancies.get(position);
        holder.setItems(vacancy);
    }

    @Override
    public int getItemCount() {
        return vacancies.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static class VacancyViewHolder extends RecyclerView.ViewHolder {

        private final TextView jobPosition;
        private final TextView companyName;

        public VacancyViewHolder(@NonNull View itemView, View.OnClickListener onClickListener) {
            super(itemView);

            itemView.findViewById(R.id.vacancy_container)
                    .setOnClickListener(onClickListener);
            jobPosition = itemView.findViewById(R.id.vacancy_title);
            companyName = itemView.findViewById(R.id.vacancy_description);
        }

        public void setItems(Vacancy vacancy) {
            jobPosition.setText(vacancy.getName());
            companyName.setText(vacancy.getLocation());
        }
    }
}
