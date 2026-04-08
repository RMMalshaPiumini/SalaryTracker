package com.techsalary.salarysubmissionservice.repository;

import com.techsalary.salarysubmissionservice.model.SalarySubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SalarySubmissionRepository extends JpaRepository<SalarySubmission, Long> {
    List<SalarySubmission> findByStatus(String status);
}