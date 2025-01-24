package com.example.candidate.repository;

import com.example.candidate.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CanRepo extends JpaRepository<Candidate, Integer> {
    @Query("SELECT c.id, c.firstName, c.age, c.city, c.gender from Candidate c")
    List<Object[]> getCandidates();

    void deleteById(int id);
    //@Query("SELECT p.id as id, p.firstName as firstName, p.age as age, e.graduation as graduation, p.gender as gender FROM Candidate c JOIN c.graduation")
}
