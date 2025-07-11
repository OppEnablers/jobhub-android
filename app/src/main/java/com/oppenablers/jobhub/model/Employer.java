package com.oppenablers.jobhub.model;

public class Employer extends User {

    private final String companyName;
    private final String companyAddress;

    public Employer(String userId, String email, String phoneNumber, String companyName, String companyAddress) {
        super(userId, email, phoneNumber);
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
