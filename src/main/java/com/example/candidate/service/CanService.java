package com.example.candidate.service;

import com.example.candidate.entity.Candid;
import com.example.candidate.entity.Candidate;
import com.example.candidate.entity.Experience;
import com.example.candidate.repository.CanRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class CanService {

    @Autowired
    private CanRepo canRepo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private com.example.candidate.repository.expRepo expRepo;


//    public Candidate saveCandidate(Candidate candidate) {
//        return canRepo.save(candidate);
//    }

    public Candidate saveCandidate(Candidate candidate) {
        Candidate savedCandidate = canRepo.save(candidate);
        String subject = "Welcome to Candidate Portal";
        String body = "Dear " + savedCandidate.getFirstName() + ",\n\n" +
                "Thank you for registering. \nYour Candidate ID is: " + savedCandidate.getId() + ".\n\n" +
                "Best Regards,\nCandidate Team";

        emailService.sendSimpleMail(savedCandidate.getEmail(), subject, body);
        return savedCandidate;
    }

    public List<Map<String, Object>> getMain(){
        List<Object[]> result = canRepo.getCandidates();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : result) {
            Map<String, Object> rowMap = new HashMap<>();
            rowMap.put("id", row[0]);
            rowMap.put("firstName", row[1]);
            rowMap.put("age", row[2]);
            rowMap.put("graduation", row[3]);
            rowMap.put("gender", row[4]);
            response.add(rowMap);
        }

        return response;
    }

    public Candidate updateCandidate(Candidate candidate) {
        canRepo.findById(candidate.getId()).orElseThrow();
        return canRepo.save(candidate);
    }

    public void updateExp(int candidateId, Candidate updatedCandidate,
                          String graduation, String graduationYear, MultipartFile resume) throws IOException {
        // Fetch Candidate
        Candidate candidate = canRepo.findById(candidateId)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found with ID: " + candidateId));

        // Update Candidate fields
        candidate.setFirstName(updatedCandidate.getFirstName());
        candidate.setLastName(updatedCandidate.getLastName());
        candidate.setEmail(updatedCandidate.getEmail());
        candidate.setPhone(updatedCandidate.getPhone());
        candidate.setCity(updatedCandidate.getCity());
        candidate.setAge(updatedCandidate.getAge());
        candidate.setGender(updatedCandidate.getGender());
        canRepo.save(candidate);

        // Fetch Experience
        Experience experience = expRepo.findByCandidateId(candidateId)
                .orElseThrow(() -> new EntityNotFoundException("Experience not found for Candidate ID: " + candidateId));

        // Update graduation and graduationYear
        experience.setGraduation(graduation);
        experience.setGraduationYear(graduationYear);

    if (resume != null && !resume.isEmpty()) {
        String uploadDir = "upload/resumes/";
        String fileName = candidateId + "_" + resume.getOriginalFilename();
        Path path = Paths.get(uploadDir + fileName);
        Files.createDirectories(path.getParent());

        // Save file
        Files.write(path, resume.getBytes());
        experience.setResume(fileName);
    }
        expRepo.save(experience);
    }

    public void deleteCandidate(int id) {
        canRepo.deleteById(id);
    }

    public Candidate getCanById(int id) {
        return canRepo.findById(id).orElseThrow();
    }

    public void saveExp(Candidate candidate, MultipartFile file, String graduation, String graduationYear) throws IOException {
        Candidate savedCandidate = canRepo.save(candidate);

        String uploadDir = "upload/resumes/";
        String fileName = savedCandidate.getId() + "_" + file.getOriginalFilename();
        Path path = Paths.get(uploadDir + fileName);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        Experience experience = new Experience();
        experience.setGraduation(graduation);
        experience.setGraduationYear(graduationYear);
        experience.setResume(fileName);
        experience.setCandidate(savedCandidate); // Link experience to the candidate

        expRepo.save(experience);

        String subject = "Welcome to Candidate Portal";
        String body = "Dear " + savedCandidate.getFirstName() + ",\n\n" +
                "Thank you for registering. \nYour Candidate ID is: " + savedCandidate.getId() + ".\n\n" +
                "Best Regards,\nCandidate Team";

        emailService.sendSimpleMail(savedCandidate.getEmail(), subject, body);
    }

    public List<Candidate> findAllCandidates() {
        return canRepo.findAll();  // Use the repository method to fetch all candidates
    }

    @Transactional
    public void deleteExperience(int id) {
        expRepo.deleteByCandidateId(id);
        canRepo.deleteById(id);
    }

    public Experience findExperienceById(int id) {
        Optional <Experience> experience = expRepo.findByCandidateId(id);

        return experience.orElseThrow(() -> new EntityNotFoundException("Experience not found for ID: " + id));
    }
}
