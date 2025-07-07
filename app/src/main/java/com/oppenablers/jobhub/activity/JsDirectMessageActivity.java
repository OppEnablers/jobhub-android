package com.oppenablers.jobhub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Map;

public class JsDirectMessageActivity extends AppCompatActivity {
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

        receiverTextView.setText(intent.getStringExtra("userName"));

        sendBtn.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString().trim();

            if (!messageText.isEmpty()) {
                String dateKey = "date_" + System.currentTimeMillis(); // or use actual date

                // Message object
                Map<String, Object> messageData = new HashMap<>();
                messageData.put("content", messageText);

                // Send to Firebase

                messageInput.setText("");
            }
        });
    }
}