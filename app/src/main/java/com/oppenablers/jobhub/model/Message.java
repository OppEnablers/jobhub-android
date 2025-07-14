package com.oppenablers.jobhub.model;

public class Message {
    public String content;
    public String senderId;
    public long timestamp;

    public Message() {
    }

    public Message(String content, String senderId, long timestamp) {
        this.content = content;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }
}
