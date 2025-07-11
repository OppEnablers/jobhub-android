package com.oppenablers.jobhub.model;

public class Job {

    private final String jobPosition;
    private final String location;

    public Job(String jobPosition, String location) {
        this.jobPosition = jobPosition;
        this.location = location;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public String getLocation() {
        return location;
    }
}
