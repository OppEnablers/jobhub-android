package com.oppenablers.jobhub.activity;

import static com.oppenablers.jobhub.model.Message.addReceivedMessageBubble;
import static com.oppenablers.jobhub.model.Message.addSentMessageBubble;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.HashMap;
import java.util.Map;

public class EmpDirectMessageActivity extends AppCompatActivity {
    LinearLayout messCont;
    ScrollView messScrollCont;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_emp_messages_direct);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_emp_messages_direct), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        TextView employee_name_tv = findViewById(R.id.application_title);
        EditText messageInput = findViewById(R.id.message_input);
        ImageButton sendBtn = findViewById(R.id.send_button);
        ImageButton addBtn = findViewById(R.id.add_button);
        String userId = intent.getStringExtra("userId");

        messCont = findViewById(R.id.messages_container);
        messScrollCont = findViewById(R.id.messages_scroll);

        employee_name_tv.setText(intent.getStringExtra("userName"));

        // Add button functionality
        addBtn.setOnClickListener(v -> {
            Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            fileIntent.setType("*/*");
            String[] mimeTypes = {"application/pdf", "image/*"};
            fileIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
            startActivityForResult(Intent.createChooser(fileIntent, "Select PDF or Image"), PICK_FILE_REQUEST);
        });

        sendBtn.setOnClickListener(v -> {
            String messageText = messageInput.getText().toString().trim();

            if (!messageText.isEmpty()) {
                String dateKey = "date_" + System.currentTimeMillis(); // or use actual date

                // Message object
                Map<String, Object> messageData = new HashMap<>();
                messageData.put("content", messageText);

                addSentMessageBubble(EmpDirectMessageActivity.this, messCont, messScrollCont, messageText, 0);

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
                .child(employeeId)
                .child(userId);

        messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    System.out.println(child);
                    Message msg = child.getValue(Message.class);
                    if (msg != null) {
                        if (msg.senderId.equals(AuthManager.getCurrentUser().getUid())) {
                            addSentMessageBubble(EmpDirectMessageActivity.this, messCont, messScrollCont, msg.content, msg.timestamp);
                        } else {
                            addReceivedMessageBubble(EmpDirectMessageActivity.this, messCont, messScrollCont, msg.content, msg.timestamp);
                        }
                    }
                }

                // Scroll to bottom
                ScrollView scroll = findViewById(R.id.messages_scroll);
                scroll.post(() -> scroll.fullScroll(View.FOCUS_DOWN));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmpDirectMessageActivity.this, "Failed to load messages.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            Intent openIntent = new Intent(Intent.ACTION_VIEW);
            openIntent.setData(fileUri);
            openIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(openIntent);
        }
    }
}
