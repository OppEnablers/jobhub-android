package com.oppenablers.jobhub.activity;

import static com.oppenablers.jobhub.model.Message.addReceivedMessageBubble;
import static com.oppenablers.jobhub.model.Message.addSentMessageBubble;

import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
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
import com.oppenablers.jobhub.BuildConfig;
import com.oppenablers.jobhub.FileManager;
import com.oppenablers.jobhub.FileUtils;
import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.model.ChatMessage;
import com.oppenablers.jobhub.model.Message;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class JsDirectMessageActivity extends AppCompatActivity {
    LinearLayout messCont;
    ScrollView messScrollCont;
    private static final int PICK_FILE_REQUEST = 1001;
    private FileManager fileManager;

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
        ImageButton addBtn = findViewById(R.id.add_button);
        String userId = intent.getStringExtra("userId");

        messCont = findViewById(R.id.messages_container);
        messScrollCont = findViewById(R.id.messages_scroll);

        receiverTextView.setText(intent.getStringExtra("userName"));

        fileManager = new FileManager(this);

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

                addSentMessageBubble(this, messCont, messScrollCont, messageText, 0);

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
                            addSentMessageBubble(JsDirectMessageActivity.this, messCont, messScrollCont, msg.content, msg.timestamp);
                        } else {
                            addReceivedMessageBubble(JsDirectMessageActivity.this, messCont, messScrollCont, msg.content, msg.timestamp);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            String fileName = System.currentTimeMillis() + "_" + (fileUri.getLastPathSegment() != null ? fileUri.getLastPathSegment() : "file");
            File file = new File(FileUtils.getPath(this, fileUri)); // You need a FileUtils.getPath() utility

            fileManager.upload(fileName, file, new FileManager.SimpleListener() {
                @Override
                public void onStateChanged(int id, com.amazonaws.mobileconnectors.s3.transferutility.TransferState state) {
                    if (state == com.amazonaws.mobileconnectors.s3.transferutility.TransferState.COMPLETED) {
                        String s3Url = "https://" + BuildConfig.BUCKET_NAME + ".s3.amazonaws.com/" + fileName;
                        boolean isImage = fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png");
                        boolean isPdf = fileName.endsWith(".pdf");

                        // Show bubble
                        if (isImage) {
                            Message.addSentMessageBubbleWithImage(JsDirectMessageActivity.this, messCont, messScrollCont, s3Url, System.currentTimeMillis());
                        } else if (isPdf) {
                            Message.addSentMessageBubbleWithPdf(JsDirectMessageActivity.this, messCont, messScrollCont, s3Url, System.currentTimeMillis());
                        }

                        // Send to Firebase
                        String userId = getIntent().getStringExtra("userId");
                        DatabaseReference messageRef = FirebaseDatabase.getInstance()
                                .getReference("messages")
                                .child(userId)
                                .child(AuthManager.getCurrentUser().getUid());

                        Message msg = new Message(s3Url, AuthManager.getCurrentUser().getUid(), System.currentTimeMillis());
                        msg.setMediaURL(true);

                        messageRef.push().setValue(msg);
                    }
                }
            });
        }
    }
}