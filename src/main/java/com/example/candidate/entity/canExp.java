package com.example.candidate.entity;

import java.util.Optional;

public class canExp {
    private Candidate candidate;
    private Experience experience;

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }
}
