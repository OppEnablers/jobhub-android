package com.oppenablers.jobhub.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.activity.JsMessagesDirectActivity;
import com.oppenablers.jobhub.adapter.ConversationAdapter;
import com.oppenablers.jobhub.databinding.FragmentJsMessagesBinding;
import com.oppenablers.jobhub.model.Conversation;
import com.oppenablers.jobhub.MessageRepository;

import java.util.ArrayList;
import java.util.List;

public class JsMessagesFragment extends Fragment {

    private FragmentJsMessagesBinding binding;
    private ConversationAdapter adapter;
    private MessageRepository messageRepository;
    private String currentUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentJsMessagesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        messageRepository = MessageRepository.getInstance();

        setupViews();
        loadConversations();
    }

    private void setupViews() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.emptyState.setVisibility(View.GONE);

        adapter = new ConversationAdapter(new ArrayList<>(), (userId, userName) -> {
            Intent intent = new Intent(getActivity(), JsMessagesDirectActivity.class);
            intent.putExtra(JsMessagesDirectActivity.EXTRA_USER_ID, userId);
            intent.putExtra(JsMessagesDirectActivity.EXTRA_USER_NAME, userName);
            startActivity(intent);
        });

        binding.messagesList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.messagesList.setAdapter(adapter);
    }

    private void loadConversations() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.emptyState.setVisibility(View.GONE);

        messageRepository.getUserConversations(currentUserId, new MessageRepository.ConversationListener() {
            @Override
            public void onConversationsLoaded(List<Conversation> conversations) {
                if (getView() == null) return;

                binding.progressBar.setVisibility(View.GONE);
                adapter.updateConversations(conversations);
                binding.emptyState.setVisibility(conversations.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(String error) {
                if (getView() == null) return;

                binding.progressBar.setVisibility(View.GONE);
                binding.emptyState.setText("Error loading conversations");
                binding.emptyState.setVisibility(View.VISIBLE);
            }
        });
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