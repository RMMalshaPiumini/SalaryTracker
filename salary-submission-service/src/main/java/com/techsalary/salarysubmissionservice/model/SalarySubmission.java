package com.techsalary.salarysubmissionservice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions", schema = "salary")
@Data
public class SalarySubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private String level;           // e.g. Junior, Mid, Senior

    @Column(nullable = false)
    private Double salary;

    @Column(nullable = false)
    private String currency;        // e.g. LKR, USD

    @Column(nullable = false)
    private Integer yearsExperience;

    // When true, company name is hidden in public search results
    @Column(nullable = false)
    private Boolean anonymize = false;

    // PENDING → APPROVED (once voting threshold is reached)
    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt = LocalDateTime.now();

    // NOTE: No email or userId stored here — privacy by design!
}