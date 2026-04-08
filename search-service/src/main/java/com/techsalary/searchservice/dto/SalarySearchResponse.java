package com.techsalary.searchservice.dto;

import com.techsalary.searchservice.model.SalarySubmission;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SalarySearchResponse {

    private Long id;
    private String country;
    private String company;   // will be "Anonymous" if anonymize=true
    private String role;
    private String level;
    private Double salary;
    private String currency;
    private Integer yearsExperience;
    private LocalDateTime submittedAt;

    // Factory method — applies anonymization rule
    public static SalarySearchResponse from(SalarySubmission s) {
        SalarySearchResponse dto = new SalarySearchResponse();
        dto.setId(s.getId());
        dto.setCountry(s.getCountry());
        dto.setRole(s.getRole());
        dto.setLevel(s.getLevel());
        dto.setSalary(s.getSalary());
        dto.setCurrency(s.getCurrency());
        dto.setYearsExperience(s.getYearsExperience());
        dto.setSubmittedAt(s.getSubmittedAt());

        // Anonymize toggle — hide company name if requested
        dto.setCompany(Boolean.TRUE.equals(s.getAnonymize())
                ? "Anonymous"
                : s.getCompany());

        return dto;
    }
}