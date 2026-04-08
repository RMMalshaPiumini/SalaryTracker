package com.techsalary.searchservice.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions", schema = "salary")
@Data
public class SalarySubmission {

    @Id
    private Long id;
    private String country;
    private String company;
    private String role;
    private String level;
    private Double salary;
    private String currency;
    private Integer yearsExperience;
    private Boolean anonymize;
    private String status;
    private LocalDateTime submittedAt;
}