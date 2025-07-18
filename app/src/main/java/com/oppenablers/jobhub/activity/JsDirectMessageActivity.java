package com.oppenablers.jobhub.activity;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.oppenablers.jobhub.AuthManager;
import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.model.ChatMessage;
import com.oppenablers.jobhub.model.Message;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class JsDirectMessageActivity extends AppCompatActivity {
    LinearLayout messCont;
    ScrollView messScrollCont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_js_messages_direct);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_js_messages_direct), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        TextView receiverTextView = findViewById(R.id.application_title);
        EditText messageInput = findViewById(R.id.message_input);
        ImageButton sendBtn = findViewById(R.id.send_button);
        String userId = intent.getStringExtra("userId");

        messCont = findViewById(R.id.messages_container);
        messScrollCont = findViewById(R.id.messages_scroll);

        receiverTextView.setText(intent.getStringExtra("userName"));

        sendBtn.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString().trim();

            if (!messageText.isEmpty()) {
                String dateKey = "date_" + System.currentTimeMillis(); // or use actual date

                // Message object
                Map<String, Object> messageData = new HashMap<>();
                messageData.put("content", messageText);

                addSentMessageBubble(messageText, 0);

                // Send to Firebase
                ChatMessage chatMessage = new ChatMessage();
                //chatMessage.setSenderId(AuthManager.getCurrentUser());
                chatMessage.sendMessageAsEmployee(userId, AuthManager.getCurrentUser().getUid(), messageText);

                messageInput.setText("");
            }
        });

        String employeeId = AuthManager.getCurrentUser().getUid();

        // Firebase path
        DatabaseReference messageRef = FirebaseDatabase.getInstance()
                .getReference("messages")
                .child(userId)
                .child(employeeId);

        // Load messages
        messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    Message msg = child.getValue(Message.class);
                    if (msg != null) {
                        if (msg.senderId.equals(AuthManager.getCurrentUser().getUid())) {
                            addSentMessageBubble(msg.content, msg.timestamp);
                        } else {
                            addReceivedMessageBubble(msg.content, msg.timestamp);
                        }
                    }
                }

                // Scroll to bottom
                ScrollView scroll = findViewById(R.id.messages_scroll);
                scroll.post(() -> scroll.fullScroll(View.FOCUS_DOWN));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(JsDirectMessageActivity.this, "Failed to load messages.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addSentMessageBubble(String messageText, long timestamp) {
        // Find your container
        LinearLayout messagesContainer = findViewById(R.id.messages_container);

        // Outer layout for alignment
        LinearLayout outerLayout = new LinearLayout(this);
        LinearLayout.LayoutParams outerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        outerParams.setMargins(0, 0, 0,8); // match layout_marginBottom="8dp"
        outerLayout.setLayoutParams(outerParams);
        outerLayout.setGravity(Gravity.END);
        outerLayout.setOrientation(LinearLayout.VERTICAL);

        // Inner message bubble
        LinearLayout bubbleLayout = new LinearLayout(this);
        LinearLayout.LayoutParams bubbleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        bubbleParams.setMarginStart(60); // match layout_marginStart="60dp"
        bubbleLayout.setLayoutParams(bubbleParams);
        bubbleLayout.setOrientation(LinearLayout.VERTICAL);
        bubbleLayout.setPadding(12, 12, 12, 12);
        bubbleLayout.setBackgroundResource(R.drawable.sent_message_bubble);

        // Message text
        TextView messageTextView = new TextView(this);
        messageTextView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        messageTextView.setText(messageText);
        messageTextView.setTextColor(Color.WHITE);
        messageTextView.setTextSize(16f);
        messageTextView.setTypeface(ResourcesCompat.getFont(this, R.font.montserrat_medium));

        // Time text
        TextView timeTextView = new TextView(this);
        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        timeParams.setMargins(0, 4, 0, 0);
        timeTextView.setLayoutParams(timeParams);
        timeTextView.setTextColor(Color.parseColor("#e0e0e0"));
        timeTextView.setTextSize(12f);
        if (timestamp == 0) {
            timeTextView.setText(new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date()));
        } else {
            timeTextView.setText(new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date(timestamp)));
        }

        // Build view hierarchy
        bubbleLayout.addView(messageTextView);
        bubbleLayout.addView(timeTextView);
        outerLayout.addView(bubbleLayout);
        messagesContainer.addView(outerLayout);

        // Optional: auto-scroll to bottom if wrapped in a ScrollView
        ScrollView scroll = findViewById(R.id.messages_scroll);
        if (scroll != null) {
            scroll.post(() -> scroll.fullScroll(View.FOCUS_DOWN));
        }
    }

    private void addReceivedMessageBubble(String messageText, long timestamp) {
        LinearLayout messagesContainer = findViewById(R.id.messages_container);

        LinearLayout outerLayout = new LinearLayout(this);
        LinearLayout.LayoutParams outerParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        outerParams.setMargins(0, 0, 0, 8);
        outerLayout.setLayoutParams(outerParams);
        outerLayout.setGravity(Gravity.START);
        outerLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout bubbleLayout = new LinearLayout(this);
        LinearLayout.LayoutParams bubbleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        bubbleParams.setMarginEnd(60); // match your sent version 60dp start offset
        bubbleLayout.setLayoutParams(bubbleParams);
        bubbleLayout.setOrientation(LinearLayout.VERTICAL);
        bubbleLayout.setPadding(12, 12, 12, 12); // 12dp all around
        bubbleLayout.setBackgroundResource(R.drawable.received_message_bubble);

        TextView messageTextView = new TextView(this);
        messageTextView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        messageTextView.setText(messageText);
        messageTextView.setTextColor(Color.parseColor("#333333"));  // dark text
        messageTextView.setTextSize(16f);
        messageTextView.setTypeface(ResourcesCompat.getFont(this, R.font.montserrat_medium));

        TextView timeTextView = new TextView(this);
        LinearLayout.LayoutParams timeParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        timeParams.setMargins(0, 4, 0, 0);
        timeTextView.setLayoutParams(timeParams);
        timeTextView.setTextColor(Color.parseColor("#808080"));
        timeTextView.setTextSize(12f);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        if (timestamp == 0) {
            timeTextView.setText(sdf.format(new Date()));
        } else {
            timeTextView.setText(sdf.format(new Date(timestamp)));
        }

        bubbleLayout.addView(messageTextView);
        bubbleLayout.addView(timeTextView);
        outerLayout.addView(bubbleLayout);
        messagesContainer.addView(outerLayout);

        ScrollView scroll = findViewById(R.id.messages_scroll);
        if (scroll != null) {
            scroll.post(() -> scroll.fullScroll(View.FOCUS_DOWN));
        }
    }
}