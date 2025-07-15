package com.oppenablers.jobhub.model;

public class Job {

    private final String id;
    private final String employerId;
    private final String name;
    private final String companyName;
    private final String location;

    public Job(String id, String employerId, String name, String companyName, String location) {
        this.id = id;
        this.employerId = employerId;
        this.name = name;
        this.companyName = companyName;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getEmployerId() {
        return employerId;
    }

    public String getName() {
        return name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getLocation() {
        return location;
    }
}
