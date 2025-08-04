package com.oppenablers.jobhub.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oppenablers.jobhub.adapter.MessageAdapter;
import com.oppenablers.jobhub.databinding.ActivityEmpMessagesDirectBinding;
import com.oppenablers.jobhub.model.Message;
import com.oppenablers.jobhub.MessageRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmpMessagesDirectActivity extends AppCompatActivity {
    public static final String EXTRA_USER_ID = "USER_ID";
    public static final String EXTRA_USER_NAME = "USER_NAME";
    private static final int PICK_FILE_REQUEST = 1001;

    private ActivityEmpMessagesDirectBinding binding;
    private MessageAdapter messageAdapter;
    private MessageRepository messageRepository;
    private String currentUserId;
    private String otherUserId;
    private String conversationId;
    private String currentUserName;
    private String otherUserName;
    private List<Message> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmpMessagesDirectBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        messageRepository = new MessageRepository();

        Intent intent = getIntent();
        if (!intent.hasExtra(EXTRA_USER_ID)) {
            finish();
            return;
        }

        otherUserId = intent.getStringExtra(EXTRA_USER_ID);
        otherUserName = intent.getStringExtra(EXTRA_USER_NAME);
        conversationId = messageRepository.generateConversationId(currentUserId, otherUserId);

        fetchCurrentUserName();

        updateTitle();
        binding.returnButton.setOnClickListener(v -> finish());

        messageAdapter = new MessageAdapter(currentUserId, messageList);
        binding.messagesList.setLayoutManager(new LinearLayoutManager(this));
        binding.messagesList.setAdapter(messageAdapter);

        setupMessageInput();

        loadMessages();

        observeNewMessages();
    }

    private void fetchCurrentUserName() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUserName = prefs.getString("currentUserName", null);

        if (currentUserName == null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUserId);
            userRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = snapshot.getValue(String.class);
                    if (name != null && !name.isEmpty()) {
                        currentUserName = name;
                        prefs.edit().putString("currentUserName", name).apply();
                        updateTitle();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    currentUserName = "Employer";
                    updateTitle();
                }
            });
        }
    }

    private void updateTitle() {
        if (otherUserName != null && !otherUserName.isEmpty()) {
            binding.applicationTitle.setText(otherUserName);
        } else {
            binding.applicationTitle.setText("User");
        }
    }

    private void setupMessageInput() {
        binding.sendButton.setOnClickListener(v -> {
            String messageText = binding.messageInput.getText().toString().trim();
            if (!messageText.isEmpty()) {
                binding.sendButton.setEnabled(false);
                sendMessage(messageText);
            } else {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        binding.addButton.setOnClickListener(v -> {
            Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            fileIntent.setType("*/*");
            String[] mimeTypes = {"application/pdf", "image/*"};
            fileIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            startActivityForResult(Intent.createChooser(fileIntent, "Select PDF or Image"), PICK_FILE_REQUEST);
        });
    }

    private void sendMessage(String messageText) {
        if (currentUserName == null || currentUserName.isEmpty()) {
            currentUserName = "Employer";
        }
        if (otherUserName == null || otherUserName.isEmpty()) {
            otherUserName = "User";
        }

        Message message = new Message(
                currentUserId,
                otherUserId,
                currentUserName,
                otherUserName,
                messageText,
                Message.MessageType.TEXT
        );

        messageRepository.sendMessage(conversationId, message, task -> {
            binding.sendButton.setEnabled(true);

            if (task.isSuccessful()) {
                binding.messageInput.setText("");
            } else {
                Toast.makeText(
                        EmpMessagesDirectActivity.this,
                        "Failed to send: " + task.getException().getMessage(),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void loadMessages() {
        Toast.makeText(this, "Loading messages...", Toast.LENGTH_SHORT).show();

        messageRepository.getMessages(conversationId, new MessageRepository.MessageListener() {
            @Override
            public void onMessagesLoaded(List<Message> messages) {
                Collections.sort(messages, (m1, m2) ->
                        Long.compare(m1.getTimestamp(), m2.getTimestamp()));

                if (!messages.isEmpty()) {
                    // Update names from existing messages if needed
                    Message firstMessage = messages.get(0);
                    if (firstMessage.getSenderId().equals(currentUserId)) {
                        currentUserName = firstMessage.getSenderName();
                        otherUserName = firstMessage.getReceiverName();
                    } else {
                        currentUserName = firstMessage.getReceiverName();
                        otherUserName = firstMessage.getSenderName();
                    }
                    updateTitle();
                }

                messageList.clear();
                messageList.addAll(messages);
                messageAdapter.updateMessages(messageList);

                if (!messageList.isEmpty()) {
                    binding.messagesList.postDelayed(() ->
                                    binding.messagesList.smoothScrollToPosition(messageList.size() - 1),
                            100);
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(EmpMessagesDirectActivity.this,
                        "Failed to load messages: " + error,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeNewMessages() {
        messageRepository.observeNewMessages(conversationId, new MessageRepository.MessageListener() {
            @Override
            public void onMessagesLoaded(List<Message> messages) {
                for (Message newMessage : messages) {
                    boolean exists = false;
                    for (Message existing : messageList) {
                        if (existing.getMessageId() != null &&
                                existing.getMessageId().equals(newMessage.getMessageId())) {
                            exists = true;
                            break;
                        }
                    }

                    if (!exists) {
                        messageList.add(newMessage);
                        messageAdapter.addMessage(newMessage);
                    }
                }

                binding.messagesList.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                messageRepository.markMessagesAsRead(conversationId, currentUserId);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(EmpMessagesDirectActivity.this,
                        "Connection error: " + error,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            Toast.makeText(this, "File selected", Toast.LENGTH_SHORT).show();
            // TODO: Implement file upload
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        messageRepository.removeConversationListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        messageRepository.removeConversationListener();
    }
}