package com.oppenablers.jobhub.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class JobSeeker extends User {

    private String name;
    private long birthday;
    private final List<String> skills;
    private final List<Integer> interests;
    private String objectives;
    private String workExperience;

    public JobSeeker(String userId, String email, String phoneNumber) {
        super(userId, email, phoneNumber);
        skills = new ArrayList<>();
        interests = new ArrayList<>();
    }

    public JobSeeker(String userId,
                     String email,
                     String phoneNumber,
                     String name,
                     long birthday,
                     List<String> skills,
                     List<Integer> interests, String objectives, String workExperience) {
        super(userId, email, phoneNumber);
        this.name = name;
        this.birthday = birthday;
        this.skills = skills;
        this.interests = interests;
        this.objectives = objectives;
        this.workExperience = workExperience;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBirthday() {
        return birthday;
    }

    public List<String> getSkills() {
        return skills;
    }

    public List<Integer> getInterests() {
        return interests;
    }

    public String getObjectives() {
        return objectives;
    }

    public void setObjectives(String objectives) {
        this.objectives = objectives;
    }

    public String getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience;
    }
}
