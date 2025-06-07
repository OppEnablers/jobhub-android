package com.oppenablers.jobhub.model;

public class Job {

    private final String jobPosition;
    private final String companyName;

    public Job(String jobPosition, String companyName) {
        this.jobPosition = jobPosition;
        this.companyName = companyName;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public String getCompanyName() {
        return companyName;
    }
}
