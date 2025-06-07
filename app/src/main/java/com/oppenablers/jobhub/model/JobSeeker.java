package com.oppenablers.jobhub.model;

import java.util.ArrayList;
import java.util.List;

public class JobSeeker extends User {

    private final String name;
    private final long birthday;
    private final List<String> skills;

    public JobSeeker(String userId, String email, String name, long birthday, List<String> skills) {
        super(userId, email);
        this.name = name;
        this.birthday = birthday;
        this.skills = skills;
    }

    public String getName() {
        return name;
    }

    public long getBirthday() {
        return birthday;
    }

    public List<String> getSkills() {
        return skills;
    }
}
