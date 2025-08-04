package com.oppenablers.jobhub.model;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Message {
    public enum MessageType {
        TEXT,
        IMAGE,
        PDF,
        // Add more if needed
    }

    private String messageId;
    private String senderId;
    private String receiverId;
    private String content;
    private long timestamp;
    private MessageType type;
    private Map<String, String> metadata;
    private boolean isRead;
    private String senderName;
    private String receiverName;

    public Message() {
        this.metadata = new HashMap<>();
        this.timestamp = new Date().getTime();
    }

    public Message(String senderId, String receiverId, String senderName, String receiverName,
                   String content, MessageType type) {
        this();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.content = content;
        this.type = type;
    }

    public Message(String senderId, String receiverId, String content, MessageType type) {
        this();
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.type = type;
        this.senderName = "";
        this.receiverName = "";
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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

    public MessageType getType() {
        return type;
    }

    public Map<String, String> getMetadata() {
        if (metadata == null) {
            metadata = new HashMap<>();
        }
        return metadata;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getSenderName() {
        return senderName != null ? senderName : "";
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiverName() {
        return receiverName != null ? receiverName : "";
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public void addMetadata(String key, String value) {
        getMetadata().put(key, value);
    }

    public String getMetadataValue(String key) {
        return getMetadata().get(key);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("messageId", messageId);
        result.put("senderId", senderId);
        result.put("receiverId", receiverId);
        result.put("senderName", getSenderName());
        result.put("receiverName", getReceiverName());
        result.put("content", content);
        result.put("timestamp", timestamp);
        result.put("type", type != null ? type.name() : MessageType.TEXT.name());
        result.put("metadata", getMetadata());
        result.put("isRead", isRead);
        return result;
    }

    public static Message fromMap(Map<String, Object> map) {
        Message message = new Message();
        if (map == null) return message;

        message.messageId = (String) map.get("messageId");
        message.senderId = (String) map.get("senderId");
        message.receiverId = (String) map.get("receiverId");
        message.senderName = (String) map.get("senderName");
        message.receiverName = (String) map.get("receiverName");
        message.content = (String) map.get("content");

        Object timestamp = map.get("timestamp");
        if (timestamp instanceof Long) {
            message.timestamp = (Long) timestamp;
        }

        try {
            message.type = MessageType.valueOf((String) map.get("type"));
        } catch (Exception e) {
            message.type = MessageType.TEXT;
        }

        Object metadata = map.get("metadata");
        if (metadata instanceof Map) {
            message.metadata = (Map<String, String>) metadata;
        }

        Object isRead = map.get("isRead");
        if (isRead instanceof Boolean) {
            message.isRead = (Boolean) isRead;
        }

        return message;
    }
}