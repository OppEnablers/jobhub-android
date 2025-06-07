package com.oppenablers.jobhub.model;

public class Employer extends User {

    private final String companyName;
    private final String companyAddress;

    public Employer(String userId, String email, String companyName, String companyAddress) {
        super(userId, email);
        this.companyName = companyName;
        this.companyAddress = companyAddress;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }
}
