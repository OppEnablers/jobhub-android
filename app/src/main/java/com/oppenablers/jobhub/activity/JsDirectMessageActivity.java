package com.oppenablers.jobhub.activity;

import static androidx.core.util.TypedValueCompat.dpToPx;

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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.databinding.ActivityMainBinding;

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

                addSentMessageBubble(messageText);

                // Send to Firebase

                messageInput.setText("");
            }
        });
    }

    private void addSentMessageBubble(String messageText) {
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
        timeTextView.setText(new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date()));

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
}