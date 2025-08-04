package com.oppenablers.jobhub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.databinding.ItemVacancyBinding;
import com.oppenablers.jobhub.model.Job;
import com.oppenablers.jobhub.model.Vacancy;

import java.util.List;

public class VacancyAdapter extends RecyclerView.Adapter<VacancyAdapter.VacancyViewHolder> {

    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    private final List<Vacancy> vacancies;
    private OnClickListener onClickListener;

    public VacancyAdapter(List<Vacancy> jobs) {
        this.vacancies = jobs;
        viewBinderHelper.setOpenOnlyOne(true);
    }

    @NonNull
    @Override
    public VacancyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new VacancyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vacancy, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VacancyViewHolder holder, int position) {
        Vacancy vacancy = vacancies.get(position);
        holder.setItems(vacancy, position, onClickListener);
        viewBinderHelper.bind(holder.binding.swipeLayout, vacancy.getId());
    }

    @Override
    public int getItemCount() {
        return vacancies.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public static class VacancyViewHolder extends RecyclerView.ViewHolder {

        private final ItemVacancyBinding binding;

        public VacancyViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemVacancyBinding.bind(itemView);

        }

        public void setItems(Vacancy vacancy, int position, OnClickListener onClickListener) {
            binding.vacancyTitle.setText(vacancy.getName());
            binding.vacancyDescription.setText(vacancy.getLocation());
            binding.vacancyContainer.setOnClickListener(v -> {
                onClickListener.onClick(vacancy.getId());
            });
            binding.deleteButton.setOnClickListener(v -> onClickListener.onDeleteClick(vacancy.getId(), position));
        }
    }

    public interface OnClickListener {
        void onClick(String vacancyId);
        void onDeleteClick(String vacancyId, int position);
    }
}
