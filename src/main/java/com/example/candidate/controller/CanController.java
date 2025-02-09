package com.example.candidate.controller;

import com.example.candidate.entity.Candid;
import com.example.candidate.entity.Candidate;
import com.example.candidate.entity.Experience;
import com.example.candidate.entity.canExp;
import com.example.candidate.repository.CanRepo;
import com.example.candidate.service.CanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
//"http://localhost:4200/"

@RestController
@CrossOrigin("http://192.168.1.188")
public class CanController {

    @Autowired
    private CanService canService;
    @Autowired
    private com.example.candidate.repository.expRepo expRepo;

    @Value("${file.upload-dir}")
    private String resumeDir;
    @Autowired
    private CanRepo canRepo;

    @PostMapping("/save/candidate")
    public Candidate addCandidate(@RequestBody Candidate candidate) {
        return canService.saveCandidate(candidate);
    }

    @PostMapping("save/exp")
    public ResponseEntity<?> saveExperience(@RequestPart("candidate") Candidate candidate, @RequestPart("file") MultipartFile file, @RequestParam("graduation") String graduation, @RequestParam("graduationYear") String graduationYear) {
        Map<String, String> response = new HashMap<>();

        if(canRepo.existsByEmail(candidate.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        try {
            canService.saveExp(candidate, file, graduation, graduationYear);
            response.put("message","Success");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("message", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

//    @PostMapping("/test")
//    public ResponseEntity<String> test(@RequestBody Candid candid) {
//
//        Candidate candidate = new Candidate();
//        candidate.setAge(candid.getAge());
//        candidate.setCity(candid.getCity());
//        candidate.setEmail(candid.getEmail());
//        candidate.setGender(candid.getGender());
//        candidate.setFirstName(candid.getFirstName());
//        candidate.setLastName(candid.getLastName());
//        candidate.setPhone(candid.getPhone());
//        canService.saveCandidate(candidate);
//
//        Experience experience = new Experience();
//        experience.setGraduationYear(candid.getGraduationYear());
//        experience.setCandidate(candidate);
//        experience.setGraduation(candid.getGraduation());
//        expRepo.save(experience);
//        return ResponseEntity.ok("success");
//    }


    @GetMapping("get/main")
    public List<Map<String, Object>> getMain() {
        return canService.getMain();
    }

    @PutMapping("/update/candidate")
    public Candidate updateCandidate(@RequestBody Candidate candidate) {
        return canService.updateCandidate(candidate);
    }

    @PutMapping("/update/exp/{id}")
    public ResponseEntity<Map<String, String>> updateExp(
            @PathVariable int id,
            @RequestPart("candidate") Candidate candidate, // JSON part
            @RequestParam String graduation,
            @RequestParam String graduationYear,
    @RequestPart(value = "resume", required = false) MultipartFile resume) throws IOException {

        canService.updateExp(id, candidate, graduation, graduationYear, resume   );

        Map<String, String> response = new HashMap<>();
        response.put("message", "Candidate and experience updated successfully!");
        return ResponseEntity.ok(response);    }

    @DeleteMapping("/delete/candidate/{id}")
    public void deleteCandidate(@PathVariable int id) {
        canService.deleteCandidate(id);
    }

    @DeleteMapping("/delete/exp/{id}")
    public void deleteExperience(@PathVariable int id) throws IOException {
        canService.deleteExperience(id);
    }

    @GetMapping("get/{id}")
    public Candidate getCandidate(@PathVariable int id) {
        return canService.getCanById(id);
    }

    @GetMapping("/resume/{fileName}")
    public ResponseEntity<Resource> getResume(@PathVariable String fileName) throws IOException {
        Path filePath = Paths.get(resumeDir + "/" + fileName);
        Resource resource = new UrlResource(filePath.toUri());
        System.out.println("Looking for file at: " + filePath.toString());

        if (!resource.exists()) {
            throw new FileNotFoundException("File not found: " + fileName);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }

    @GetMapping("get/exp")
    public ResponseEntity<List<Candid>> getExp() {
        List<Candidate> candidates = canService.findAllCandidates();
        List<Candid> candidateDetailsList = new ArrayList<>();

        for (Candidate candidate : candidates) {
            Optional<Experience> experienceOpt = expRepo.findByCandidateId(candidate.getId());
            if (experienceOpt.isPresent()) {
                Experience experience = experienceOpt.get();

                Candid candidateDetailsDTO = new Candid();
                candidateDetailsDTO.setId(candidate.getId());
                candidateDetailsDTO.setFirstName(candidate.getFirstName());
                candidateDetailsDTO.setLastName(candidate.getLastName());
                candidateDetailsDTO.setAge(candidate.getAge());
                candidateDetailsDTO.setEmail(candidate.getEmail());
                candidateDetailsDTO.setCity(candidate.getCity());
                candidateDetailsDTO.setPhone(candidate.getPhone());
                candidateDetailsDTO.setGender(candidate.getGender());
                candidateDetailsDTO.setGraduation(experience.getGraduation());
                candidateDetailsDTO.setGraduationYear(experience.getGraduationYear());
                candidateDetailsDTO.setResume(experience.getResume());

                candidateDetailsList.add(candidateDetailsDTO);
            }
        }

        return ResponseEntity.ok(candidateDetailsList);
    }

    @GetMapping("/canExp/{id}")
    public ResponseEntity<canExp> getCanExp(@PathVariable int id) {
        Candidate candidate = canService.getCanById(id);
        Experience experience = canService.findExperienceById(id);

        canExp ce = new canExp();
        ce.setCandidate(candidate);
        ce.setExperience(experience);
        return ResponseEntity.ok(ce);
    }

    @GetMapping("/canExp/email/{id}")
    public ResponseEntity<canExp> getCanExpEmail(@PathVariable String id) {
        return ResponseEntity.ok(   canService.getCanExpByMail(id));
    }

    @DeleteMapping("/delete/email/{email}")
    public ResponseEntity<Map<String, String>> deleteCandidateByEmail(@PathVariable String email) throws IOException {
        canService.deleteByEmail(email);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Candidate and associated details deleted successfully!");
        return ResponseEntity.ok(response);    }

    @GetMapping("/check/mail/{email}")
    public ResponseEntity<Boolean> checkMail(@PathVariable String email) {
        boolean emailExists = canService.emailExists(email);
        return ResponseEntity.ok(emailExists);
    }

}
