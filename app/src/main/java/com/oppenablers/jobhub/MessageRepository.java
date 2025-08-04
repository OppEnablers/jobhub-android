package com.oppenablers.jobhub;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.oppenablers.jobhub.model.Conversation;
import com.oppenablers.jobhub.model.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageRepository {

    private static MessageRepository instance;
    private static final String MESSAGES_PATH = "messages";
    private static final String USER_CONVERSATIONS_PATH = "user_conversations";
    private final DatabaseReference databaseReference;
    private ValueEventListener activeConversationListener;
    private DatabaseReference currentConversationRef;

    public MessageRepository() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public static MessageRepository getInstance() {
        if (instance == null) instance = new MessageRepository();
        return instance;
    }

    public interface MessageListener {
        void onMessagesLoaded(List<Message> messages);

        void onError(String error);
    }

    public interface SimpleListener {
        void onComplete(boolean success);
    }

    public void sendMessage(String conversationId, Message message, OnCompleteListener<Void> listener) {
        DatabaseReference messagesRef = databaseReference.child("messages")
                .child(conversationId)
                .push();

        message.setMessageId(messagesRef.getKey());
        messagesRef.setValue(message.toMap())
                .addOnCompleteListener(listener);

        updateConversationMetadata(message.getSenderId(), message.getReceiverId(), message);
        updateConversationMetadata(message.getReceiverId(), message.getSenderId(), message);
    }

    public void getMessages(String conversationId, MessageListener listener) {
        databaseReference.child("messages")
                .child(conversationId)
                .orderByChild("timestamp")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Message> messages = new ArrayList<>();
                        for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                            Message message = messageSnapshot.getValue(Message.class);
                            if (message != null) {
                                message.setMessageId(messageSnapshot.getKey());
                                messages.add(message);
                            }
                        }
                        listener.onMessagesLoaded(messages);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onError(error.getMessage());
                    }
                });
    }

    public void observeNewMessages(String conversationId, MessageListener listener) {
        databaseReference.child(MESSAGES_PATH).child(conversationId)
                .orderByChild("timestamp")
                .limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Message> messages = new ArrayList<>();
                        for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                            Message message = messageSnapshot.getValue(Message.class);
                            if (message != null) {
                                messages.add(message);
                            }
                        }
                        listener.onMessagesLoaded(messages);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onError(error.getMessage());
                    }
                });
    }

    public void markMessagesAsRead(String conversationId, String currentUserId) {
        Query unreadMessagesQuery = databaseReference.child(MESSAGES_PATH)
                .child(conversationId)
                .orderByChild("isRead")
                .equalTo(false);

        unreadMessagesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot messageSnapshot : snapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    if (message != null && !message.getSenderId().equals(currentUserId)) {
                        messageSnapshot.getRef().child("isRead").setValue(true);

                        updateReadStatusInConversation(
                                currentUserId,
                                message.getSenderId(),
                                messageSnapshot.getKey()
                        );
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void markAsRead(String messageId, String conversationId) {
        databaseReference.child(MESSAGES_PATH)
                .child(conversationId)
                .child(messageId)
                .child("isRead")
                .setValue(true);
    }

    private void updateReadStatusInConversation(String userId, String otherUserId, String messageId) {
        String conversationId = generateConversationId(userId, otherUserId);
        databaseReference.child(USER_CONVERSATIONS_PATH)
                .child(userId)
                .child(conversationId)
                .child("hasUnreadMessages")
                .setValue(false);
    }

    public String generateConversationId(String userId1, String userId2) {
        return userId1.compareTo(userId2) < 0 ?
                userId1 + "_" + userId2 :
                userId2 + "_" + userId1;
    }

    private void updateConversationMetadata(String userId, String otherUserId, Message message) {
        String conversationId = generateConversationId(userId, otherUserId);
        DatabaseReference conversationRef = databaseReference.child("user_conversations")
                .child(userId)
                .child(conversationId);

        Map<String, Object> updates = new HashMap<>();
        updates.put("lastMessageContent", message.getContent());
        updates.put("lastMessageTimestamp", message.getTimestamp());
        updates.put("hasUnreadMessages", !userId.equals(message.getSenderId()));

        updates.put("otherUserId", otherUserId);
        updates.put("otherUserName", userId.equals(message.getSenderId())
                ? message.getReceiverName()
                : message.getSenderName());

        conversationRef.updateChildren(updates);
    }

    public interface ConversationListener {
        void onConversationsLoaded(List<Conversation> conversations);

        void onError(String error);
    }

    public void getUserConversations(String userId, ConversationListener listener) {
        if (activeConversationListener != null && currentConversationRef != null) {
            currentConversationRef.removeEventListener(activeConversationListener);
        }

        currentConversationRef = databaseReference.child("user_conversations").child(userId);
        activeConversationListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Conversation> conversations = new ArrayList<>();
                for (DataSnapshot conversationSnapshot : snapshot.getChildren()) {
                    Conversation conversation = conversationSnapshot.getValue(Conversation.class);
                    if (conversation != null) {
                        conversation.setConversationId(conversationSnapshot.getKey());
                        conversations.add(conversation);
                    }
                }
                listener.onConversationsLoaded(conversations);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.getMessage());
            }
        };

        currentConversationRef.orderByChild("lastMessageTimestamp")
                .addValueEventListener(activeConversationListener);
    }

    public void removeConversationListener() {
        if (activeConversationListener != null && currentConversationRef != null) {
            currentConversationRef.removeEventListener(activeConversationListener);
            activeConversationListener = null;
            currentConversationRef = null;
        }
    }

    public void createTestConversation(String employerId, String jobseekerId, String employerName, String jobseekerName) {
        String conversationId = generateConversationId(employerId, jobseekerId);

        List<Message> testMessages = Arrays.asList(
                new Message(
                        jobseekerId,
                        employerId,
                        jobseekerName,
                        employerName,
                        "Hello, I'm interested in the job position!",
                        Message.MessageType.TEXT
                ),
                new Message(
                        employerId,
                        jobseekerId,
                        employerName,
                        jobseekerName,
                        "Thanks for applying! When can you come for an interview?",
                        Message.MessageType.TEXT
                ),
                new Message(
                        jobseekerId,
                        employerId,
                        jobseekerName,
                        employerName,
                        "How about tomorrow at 2 PM?",
                        Message.MessageType.TEXT
                )
        );

        DatabaseReference convRef = databaseReference.child(MESSAGES_PATH).child(conversationId);

        for (Message message : testMessages) {
            String messageId = convRef.push().getKey();
            message.setMessageId(messageId);
            convRef.child(messageId).setValue(message.toMap());

            updateConversationMetadata(employerId, jobseekerId, message);
            updateConversationMetadata(jobseekerId, employerId, message);
        }
    }

    public interface TestConversationCallback {
        void onSuccess();

        void onFailure(String error);
    }

    public void createEmptyConversation(String user1Id, String user2Id,
                                        String user1Name, String user2Name,
                                        TestConversationCallback callback) {
        String conversationId = generateConversationId(user1Id, user2Id);
        DatabaseReference convRef = databaseReference.child(MESSAGES_PATH).child(conversationId);
        Message initialMessage = new Message(
                user1Id,
                user2Id,
                user1Name,
                user2Name,
                "Hello, you have been accepted.",
                Message.MessageType.TEXT
        );

        String messageId = convRef.push().getKey();

        if (messageId == null) {
            callback.onFailure("Message ID was null.");
            return;
        }

        initialMessage.setMessageId(messageId);
        convRef.child(messageId).setValue(initialMessage.toMap())
                .addOnFailureListener(e -> {
                    callback.onFailure(e.getMessage());
                });

        updateConversationMetadata(user1Id, user2Id, initialMessage);
        updateConversationMetadata(user2Id, user1Id, initialMessage);

        callback.onSuccess();
    }

    public void createTestConversation(String user1Id, String user2Id,
                                       String user1Name, String user2Name,
                                       TestConversationCallback callback) {
        String conversationId = generateConversationId(user1Id, user2Id);

        List<Message> testMessages = Arrays.asList(
                new Message(
                        user1Id,
                        user2Id,
                        user1Name,
                        user2Name,
                        "Hello, this is a test message",
                        Message.MessageType.TEXT
                ),
                new Message(
                        user2Id,
                        user1Id,
                        user2Name,
                        user1Name,
                        "This is a test reply",
                        Message.MessageType.TEXT
                )
        );

        DatabaseReference convRef = databaseReference.child(MESSAGES_PATH).child(conversationId);

        for (Message message : testMessages) {
            String messageId = convRef.push().getKey();
            message.setMessageId(messageId);
            convRef.child(messageId).setValue(message.toMap())
                    .addOnFailureListener(e -> {
                        callback.onFailure(e.getMessage());
                        return;
                    });

            updateConversationMetadata(user1Id, user2Id, message);
            updateConversationMetadata(user2Id, user1Id, message);
        }

        callback.onSuccess();
    }
}