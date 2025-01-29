package com.example.candidate.repository;

import com.example.candidate.entity.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface expRepo extends JpaRepository<Experience, Long> {
    Optional <Experience> findByCandidateId(int candidate_id);

    void deleteByCandidateId(int candid_id);

    void deleteByCandidateEmail(String candidate_email);

    Optional<Object> findByCandidateEmail(String candidate_email);
}
