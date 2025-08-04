package com.oppenablers.jobhub.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oppenablers.jobhub.adapter.MessageAdapter;
import com.oppenablers.jobhub.databinding.ActivityJsMessagesDirectBinding;
import com.oppenablers.jobhub.model.Message;
import com.oppenablers.jobhub.MessageRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsMessagesDirectActivity extends AppCompatActivity {
    public static final String EXTRA_USER_ID = "USER_ID";
    public static final String EXTRA_USER_NAME = "USER_NAME";
    private static final int PICK_FILE_REQUEST = 1001;

    private ActivityJsMessagesDirectBinding binding;
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
        binding = ActivityJsMessagesDirectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        messageRepository = MessageRepository.getInstance();

        Intent intent = getIntent();
        if (!intent.hasExtra(EXTRA_USER_ID)) {
            finish();
            return;
        }

        otherUserId = intent.getStringExtra(EXTRA_USER_ID);
        otherUserName = intent.getStringExtra(EXTRA_USER_NAME);
        conversationId = messageRepository.generateConversationId(currentUserId, otherUserId);
        currentUserName = "Current User Name";

        binding.applicationTitle.setText(otherUserName);
        binding.returnButton.setOnClickListener(v -> finish());

        messageAdapter = new MessageAdapter(currentUserId, messageList);
        binding.messagesList.setLayoutManager(new LinearLayoutManager(this));
        binding.messagesList.setAdapter(messageAdapter);

        setupMessageInput();

        Toast.makeText(this, "Loading messages...", Toast.LENGTH_SHORT).show();
        loadMessages();

        observeNewMessages();

        fetchCurrentUserName();
    }

    private void fetchCurrentUserName() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUserName = prefs.getString("currentUserName", null);
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
                        JsMessagesDirectActivity.this,
                        "Failed to send: " + task.getException().getLocalizedMessage(),
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void loadMessages() {
        messageRepository.getMessages(conversationId, new MessageRepository.MessageListener() {
            @Override
            public void onMessagesLoaded(List<Message> messages) {
                Collections.sort(messages, (m1, m2) ->
                        Long.compare(m1.getTimestamp(), m2.getTimestamp()));

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
                Toast.makeText(
                        JsMessagesDirectActivity.this,
                        "Error loading messages: " + error,
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }

    private void observeNewMessages() {
        messageRepository.observeNewMessages(conversationId, new MessageRepository.MessageListener() {
            @Override
            public void onMessagesLoaded(List<Message> messages) {
                for (Message newMessage : messages) {
                    boolean messageExists = false;
                    for (Message existingMessage : messageList) {
                        if (existingMessage.getMessageId() != null &&
                                existingMessage.getMessageId().equals(newMessage.getMessageId())) {
                            messageExists = true;
                            break;
                        }
                    }

                    if (!messageExists) {
                        messageList.add(newMessage);
                        messageAdapter.addMessage(newMessage);
                    }
                }

                binding.messagesList.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                messageRepository.markMessagesAsRead(conversationId, currentUserId);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(
                        JsMessagesDirectActivity.this,
                        "Connection error: " + error,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            Toast.makeText(this, "Preparing file...", Toast.LENGTH_SHORT).show();

            // TODO: Implement actual file upload logic here
        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "File selection cancelled", Toast.LENGTH_SHORT).show();
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