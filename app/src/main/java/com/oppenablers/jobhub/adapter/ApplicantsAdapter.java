package com.oppenablers.jobhub.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.oppenablers.jobhub.AuthManager;
import com.oppenablers.jobhub.MessageRepository;
import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.activity.EmpMessagesDirectActivity;
import com.oppenablers.jobhub.api.JobHubClient;
import com.oppenablers.jobhub.databinding.ItemJobseekerBinding;
import com.oppenablers.jobhub.model.ApplicantData;
import com.oppenablers.jobhub.model.JobSeeker;
import com.oppenablers.jobhub.model.Vacancy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ApplicantsAdapter extends RecyclerView.Adapter<ApplicantsAdapter.ApplicantsViewHolder> {

    private ArrayList<ApplicantData> applicants;
    private ApplicantClickListener applicantClickListener;

    public ApplicantsAdapter(ArrayList<ApplicantData> applicants) {
        this.applicants = applicants;
    }

    @NonNull
    @Override
    public ApplicantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ApplicantsViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_jobseeker, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicantsViewHolder holder, int position) {
        ApplicantData applicantData = applicants.get(position);
        holder.setApplicantData(applicantData, applicantClickListener);
    }

    @Override
    public int getItemCount() {
        return applicants.size();
    }

    public void setClickListener(ApplicantClickListener applicantClickListener) {
        this.applicantClickListener = applicantClickListener;
    }

    public static class ApplicantsViewHolder extends RecyclerView.ViewHolder {

        ItemJobseekerBinding binding;
        private boolean expanded = false;

        public ApplicantsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemJobseekerBinding.bind(itemView);
        }

        public void setApplicantData(ApplicantData applicantData, ApplicantClickListener applicantClickListener) {

            Vacancy vacancy = applicantData.getVacancy();
            JobSeeker applicant = applicantData.getApplicant();

            binding.collapsedApplicantName.setText(applicant.getName());
            binding.collapsedApplicantDescription.setText(vacancy.getName());

            binding.vacancyTitle.setText(vacancy.getName());
            binding.applicantName.setText(applicant.getName());
            Date birthDate = new Date(applicant.getBirthday());
            Calendar calendar = Calendar.getInstance();
            int todayYear = calendar.get(Calendar.YEAR);
            calendar.setTime(birthDate);
            int birthYear = calendar.get(Calendar.YEAR);
            String age = String.format(Locale.getDefault(), "%d years old", todayYear - birthYear);
            binding.applicantAge.setText(age);
            binding.applicantObjectives.setText(applicant.getObjectives());

            JobHubClient.getProfilePicture(applicant.getUserId(), new JobHubClient.JobHubCallback<byte[]>() {
                @Override
                public void onFailure() {

                }

                @Override
                public void onSuccess(byte[] result) {
                    Glide.with(itemView).load(result)
                            .into(binding.collapsedImage);
                    Glide.with(itemView).load(result)
                            .into(binding.profilePicture);
                }
            });

            binding.vacancyContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expanded = !expanded;
                    if (expanded) {
                        binding.collapsedLayout.setVisibility(View.GONE);
                        binding.expandedLayout.setVisibility(View.VISIBLE);
                    } else {
                        binding.collapsedLayout.setVisibility(View.VISIBLE);
                        binding.expandedLayout.setVisibility(View.GONE);
                    }
                }
            });

            binding.messageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    applicantClickListener.onMessage(applicant.getUserId(), vacancy.getId());

                    Context context = binding.getRoot().getContext();

                    SharedPreferences prefs = context.getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    String currentUserName = prefs.getString("currentUserName", null);
                    String currentUserId =AuthManager.getCurrentUser().getUid();

                    MessageRepository messageRepository = MessageRepository.getInstance();
                    messageRepository.createEmptyConversation(
                            currentUserId,
                            applicant.getUserId(),
                            currentUserName,
                            applicant.getName(),
                            new MessageRepository.TestConversationCallback() {
                                @Override
                                public void onSuccess() {
                                    Intent intent = new Intent(context, EmpMessagesDirectActivity.class);
                                    intent.putExtra(EmpMessagesDirectActivity.EXTRA_USER_ID, applicant.getUserId());
                                    intent.putExtra(EmpMessagesDirectActivity.EXTRA_USER_NAME, applicant.getName());
                                    context.startActivity(intent);
                                }

                                @Override
                                public void onFailure(String error) {
                                }
                            }
                    );
                }
            });

            binding.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    applicantClickListener.onDelete(applicant.getUserId(), vacancy.getId());
                }
            });
        }
    }

    public interface ApplicantClickListener {
        void onMessage(String userId, String vacancyId);
        void onDelete(String userId, String vacancyId);
    }
}
