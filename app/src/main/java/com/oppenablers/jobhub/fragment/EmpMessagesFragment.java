package com.oppenablers.jobhub.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.activity.EmpMessagesDirectActivity;
import com.oppenablers.jobhub.adapter.ConversationAdapter;
import com.oppenablers.jobhub.databinding.FragmentEmpMessagesBinding;
import com.oppenablers.jobhub.model.Conversation;
import com.oppenablers.jobhub.MessageRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class EmpMessagesFragment extends Fragment {

    private FragmentEmpMessagesBinding binding;
    private ConversationAdapter adapter;
    private MessageRepository messageRepository;
    private String currentUserId;

    private static final String TEST_EMPLOYER_ID = "yhPaESRBdCct8vphE4b7de0paGo1";
    private static final String TEST_JOBSEEKER_ID = "ezGrfP1oi9dohk0qdKwpKPH9mIs2";
    private static final String TEST_EMPLOYER_NAME = "iACADEMY";
    private static final String TEST_JOBSEEKER_NAME = "Taku-chan";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmpMessagesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        messageRepository = new MessageRepository();

        setupViews();
        setupTestButton();
        loadConversations();
    }

    private void setupViews() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.emptyState.setVisibility(View.GONE);

        binding.messagesList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ConversationAdapter(new ArrayList<>(), (userId, userName) -> {
            // Ensure we always have a valid name before opening conversation
            String displayName = validateName(userName);
            Intent intent = new Intent(getActivity(), EmpMessagesDirectActivity.class);
            intent.putExtra(EmpMessagesDirectActivity.EXTRA_USER_ID, userId);
            intent.putExtra(EmpMessagesDirectActivity.EXTRA_USER_NAME, displayName);
            startActivity(intent);
        });
        binding.messagesList.setAdapter(adapter);
    }

    private String validateName(String name) {
        if (name == null || name.isEmpty()) {
            return "User";
        }
        return name;
    }

    private void loadConversations() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.emptyState.setVisibility(View.GONE);

        messageRepository.getUserConversations(currentUserId, new MessageRepository.ConversationListener() {
            @Override
            public void onConversationsLoaded(List<Conversation> conversations) {
                if (getView() == null) return;

                binding.progressBar.setVisibility(View.GONE);

                List<Conversation> validConversations = new ArrayList<>();
                for (Conversation conv : conversations) {
                    if (conv.getOtherUserId() == null || conv.getOtherUserId().isEmpty()) {
                        continue;
                    }

                    conv.setOtherUserName(validateName(conv.getOtherUserName()));
                    if (conv.getLastMessageContent() == null) {
                        conv.setLastMessageContent("No messages yet");
                    }

                    validConversations.add(conv);
                }

                adapter.updateConversations(validConversations);

                if (validConversations.isEmpty()) {
                    binding.emptyState.setText("No conversations yet");
                    binding.emptyState.setVisibility(View.VISIBLE);
                } else {
                    binding.emptyState.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(String error) {
                if (getView() == null) return;

                binding.progressBar.setVisibility(View.GONE);
                binding.emptyState.setText("Error loading conversations");
                binding.emptyState.setVisibility(View.VISIBLE);
                Log.e("ConversationError", error);
            }
        });
    }

    private void setupTestButton() {
        if (currentUserId.equals(TEST_EMPLOYER_ID) || currentUserId.equals(TEST_JOBSEEKER_ID)) {
            binding.btnCreateTest.setVisibility(View.VISIBLE);
            binding.btnCreateTest.setOnClickListener(v -> createTestConversation());
        } else {
            binding.btnCreateTest.setVisibility(View.GONE);
        }
    }

    private void createTestConversation() {
        boolean isEmployer = currentUserId.equals(TEST_EMPLOYER_ID);
        String otherUserId = isEmployer ? TEST_JOBSEEKER_ID : TEST_EMPLOYER_ID;
        String otherUserName = isEmployer ? TEST_JOBSEEKER_NAME : TEST_EMPLOYER_NAME;
        String currentUserName = isEmployer ? TEST_EMPLOYER_NAME : TEST_JOBSEEKER_NAME;

        messageRepository.createTestConversation(
                currentUserId,
                otherUserId,
                currentUserName,
                otherUserName,
                new MessageRepository.TestConversationCallback() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getContext(), "Test conversation created!", Toast.LENGTH_SHORT).show();
                        loadConversations();
                    }

                    @Override
                    public void onFailure(String error) {
                        Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
                        Log.e("TestConvError", error);
                    }
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        loadConversations();
    }

    @Override
    public void onPause() {
        super.onPause();
        messageRepository.removeConversationListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        messageRepository.removeConversationListener();
        binding = null;
    }
}