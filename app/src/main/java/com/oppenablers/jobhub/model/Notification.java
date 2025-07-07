package com.oppenablers.jobhub.model;

import java.util.Date;

public class Notification {

    private final String source;
    private final String message;
    private final Date time;

    public Notification(String sourceName, String message, Date time) {
        this.source = sourceName;
        this.message = message;
        this.time = time;
    }

    public String getSource() {
        return source;
    }

    public String getMessage() {
        return message;
    }

    public Date getTime() {
        return time;
    }
}
