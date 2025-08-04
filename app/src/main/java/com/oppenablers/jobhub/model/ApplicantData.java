package com.oppenablers.jobhub.model;

public class ApplicantData {

    private final Vacancy vacancy;
    private final JobSeeker applicant;

    public ApplicantData(Vacancy vacancy, JobSeeker applicant) {
        this.vacancy = vacancy;
        this.applicant = applicant;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public JobSeeker getApplicant() {
        return applicant;
    }
}
