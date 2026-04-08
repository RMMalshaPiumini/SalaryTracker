package com.techsalary.salarysubmissionservice.service;

import com.techsalary.salarysubmissionservice.dto.SalaryRequest;
import com.techsalary.salarysubmissionservice.model.SalarySubmission;
import com.techsalary.salarysubmissionservice.repository.SalarySubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalarySubmissionService {

    private final SalarySubmissionRepository repository;

    public SalarySubmission submit(SalaryRequest request) {
        SalarySubmission submission = new SalarySubmission();
        submission.setCountry(request.getCountry());
        submission.setCompany(request.getCompany());
        submission.setRole(request.getRole());
        submission.setLevel(request.getLevel());
        submission.setSalary(request.getSalary());
        submission.setCurrency(request.getCurrency());
        submission.setYearsExperience(request.getYearsExperience());
        submission.setAnonymize(request.getAnonymize());
        // Always starts as PENDING — never directly APPROVED
        submission.setStatus("PENDING");

        return repository.save(submission);
    }

    public List<SalarySubmission> getPending() {
        return repository.findByStatus("PENDING");
    }

    // Called by vote-service once threshold is reached
    public SalarySubmission approve(Long id) {
        SalarySubmission submission = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Submission not found: " + id));
        submission.setStatus("APPROVED");
        return repository.save(submission);
    }
}
