package com.techsalary.salarysubmissionservice.controller;

import com.techsalary.salarysubmissionservice.dto.SalaryRequest;
import com.techsalary.salarysubmissionservice.model.SalarySubmission;
import com.techsalary.salarysubmissionservice.service.SalarySubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/salaries")
@RequiredArgsConstructor
public class SalarySubmissionController {

    private final SalarySubmissionService service;

    // Anyone can submit — no login required
    @PostMapping
    public ResponseEntity<SalarySubmission> submit(
            @Valid @RequestBody SalaryRequest request) {
        return ResponseEntity.ok(service.submit(request));
    }

    // Returns all PENDING submissions for the community to vote on
    @GetMapping("/pending")
    public ResponseEntity<List<SalarySubmission>> getPending() {
        return ResponseEntity.ok(service.getPending());
    }

    // Called internally by vote-service — not exposed publicly
    @PutMapping("/{id}/approve")
    public ResponseEntity<SalarySubmission> approve(@PathVariable Long id) {
        return ResponseEntity.ok(service.approve(id));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}