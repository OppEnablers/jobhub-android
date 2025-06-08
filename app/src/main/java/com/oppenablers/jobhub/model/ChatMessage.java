package com.oppenablers.jobhub.model;

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
}