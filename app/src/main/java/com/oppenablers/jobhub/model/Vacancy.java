package com.oppenablers.jobhub.model;

public class Vacancy {

    private final String id;
    private String name;
    private String location;
    private int time;
    private int shift;
    private int modality;
    private String objectives;
    private String skills;
    private float minimumSalary;
    private float maximumSalary;
    private int workExperience;

    public Vacancy(String id,
                   String name,
                   String location,
                   int time,
                   int shift,
                   int modality,
                   String objectives,
                   String skills,
                   float minimumSalary,
                   float maximumSalary,
                   int workExperience
    ) {
        this.id = id;
        this.name = name;
        this.maximumSalary = maximumSalary;
        this.minimumSalary = minimumSalary;
        this.location = location;
        this.time = time;
        this.shift = shift;
        this.modality = modality;
        this.objectives = objectives;
        this.skills = skills;
        this.workExperience = workExperience;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getShift() {
        return shift;
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    public int getModality() {
        return modality;
    }

    public void setModality(int modality) {
        this.modality = modality;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }

    public float getMinimumSalary() {
        return minimumSalary;
    }

    public void setMinimumSalary(float minimumSalary) {
        this.minimumSalary = minimumSalary;
    }

    public float getMaximumSalary() {
        return maximumSalary;
    }

    public void setMaximumSalary(float maximumSalary) {
        this.maximumSalary = maximumSalary;
    }

    public int getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(int workExperience) {
        this.workExperience = workExperience;
    }
}
