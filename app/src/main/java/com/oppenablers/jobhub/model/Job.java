package com.oppenablers.jobhub.model;

public class Job {

    private final String id;
    private final String employerId;
    private final String name;
    private final String location;
    private final String companyName;
    private final int time;
    private final int shift;
    private final int modality;
    private final String objectives;
    private final String skills;
    private final float minimumSalary;
    private final float maximumSalary;
    private final int workExperience;

    public Job(String id,
               String employerId,
               String name,
               String location,
               String companyName,
               int time,
               int shift,
               int modality,
               String objectives,
               String skills,
               float minimumSalary,
               float maximumSalary,
               int workExperience) {
        this.id = id;
        this.employerId = employerId;
        this.name = name;
        this.companyName = companyName;
        this.location = location;
        this.time = time;
        this.shift = shift;
        this.modality = modality;
        this.objectives = objectives;
        this.skills = skills;
        this.minimumSalary = minimumSalary;
        this.maximumSalary = maximumSalary;
        this.workExperience = workExperience;
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

    public String getLocation() {
        return location;
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getTime() {
        return time;
    }

    public int getShift() {
        return shift;
    }

    public int getModality() {
        return modality;
    }

    public String getObjectives() {
        return objectives;
    }

    public String getSkills() {
        return skills;
    }

    public float getMinimumSalary() {
        return minimumSalary;
    }

    public float getMaximumSalary() {
        return maximumSalary;
    }

    public int getWorkExperience() {
        return workExperience;
    }
}
