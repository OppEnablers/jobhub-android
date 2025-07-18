package com.oppenablers.jobhub.activity;

import static com.oppenablers.jobhub.model.Message.addReceivedMessageBubble;
import static com.oppenablers.jobhub.model.Message.addSentMessageBubble;
import com.oppenablers.jobhub.FileUtils;

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
import com.oppenablers.jobhub.BuildConfig;
import com.oppenablers.jobhub.FileManager;
import com.oppenablers.jobhub.R;
import com.oppenablers.jobhub.model.ChatMessage;
import com.oppenablers.jobhub.model.Message;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class EmpDirectMessageActivity extends AppCompatActivity {
    private static final int PICK_FILE_REQUEST = 1001;
    LinearLayout messCont;
    ScrollView messScrollCont;
    private FileManager fileManager;

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
                            Message.addSentMessageBubbleWithImage(EmpDirectMessageActivity.this, messCont, messScrollCont, s3Url, System.currentTimeMillis());
                        } else if (isPdf) {
                            Message.addSentMessageBubbleWithPdf(EmpDirectMessageActivity.this, messCont, messScrollCont, s3Url, System.currentTimeMillis());
                        }

                        // Send to Firebase
                        String userId = getIntent().getStringExtra("userId");
                        DatabaseReference messageRef = FirebaseDatabase.getInstance()
                                .getReference("messages")
                                .child(AuthManager.getCurrentUser().getUid())
                                .child(userId);

                        Message msg = new Message(s3Url, AuthManager.getCurrentUser().getUid(), System.currentTimeMillis());
                        msg.setMediaURL(true);

                        messageRef.push().setValue(msg);
                    }
                }
            });
        }
    }
}
