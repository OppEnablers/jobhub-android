package com.oppenablers.jobhub.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatMessage {
    public String senderId;
    public String receiverId;
    public String content;
    public long timestamp;

    public ChatMessage() {}

    public ChatMessage(String senderId, String receiverId, String content, long timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setSenderId(FirebaseUser currentUser) {
        if (currentUser != null) {
            this.senderId = currentUser.getUid();
        } else {
            Log.e("ChatMessage", "Current user is null, cannot set senderId");
        }
    }

    public void sendMessageAsEmployee(String companyId, String employeeId, String messageText) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference messageRef = rootRef.child("messages")
                .child(companyId)
                .child(employeeId)
                .child(String.valueOf(System.currentTimeMillis()));

        ChatMessage chat = new ChatMessage(employeeId, companyId, messageText, System.currentTimeMillis());
        messageRef.setValue(chat)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Message sent"))
                .addOnFailureListener(e -> Log.e("Firebase", "Failed", e));
    }

    public void readMessage(String companyId, String employeeId, String dateKey) {
        DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference()
                .child("messages")
                .child(companyId)
                .child(employeeId)
                .child(dateKey);

        messageRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatMessage chat = snapshot.getValue(ChatMessage.class);
                if (chat != null) {
                    Log.d("Firebase", "Content: " + chat.content);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error reading", error.toException());
            }
        });
    }
}