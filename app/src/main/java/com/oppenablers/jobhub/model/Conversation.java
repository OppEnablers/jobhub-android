package com.oppenablers.jobhub.model;

import java.util.Date;

public class Conversation {
    private String conversationId;
    private String otherUserId;
    private String otherUserName;
    private String otherUserPhotoUrl;
    private String lastMessageContent;
    private long lastMessageTimestamp;
    private boolean hasUnreadMessages;

    public Conversation() {}

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getOtherUserName() {
        return otherUserName;
    }

    public void setOtherUserName(String otherUserName) {
        this.otherUserName = otherUserName;
    }

    public String getOtherUserPhotoUrl() {
        return otherUserPhotoUrl;
    }

    public void setOtherUserPhotoUrl(String otherUserPhotoUrl) {
        this.otherUserPhotoUrl = otherUserPhotoUrl;
    }

    public String getLastMessageContent() {
        return lastMessageContent;
    }

    public void setLastMessageContent(String lastMessageContent) {
        this.lastMessageContent = lastMessageContent;
    }

    public long getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(long lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public boolean hasUnreadMessages() {
        return hasUnreadMessages;
    }

    public void setHasUnreadMessages(boolean hasUnreadMessages) {
        this.hasUnreadMessages = hasUnreadMessages;
    }

    public String getMessagePreview() {
        if (lastMessageContent == null) return "";
        return lastMessageContent.length() > 30 ?
                lastMessageContent.substring(0, 30) + "..." :
                lastMessageContent;
    }

}