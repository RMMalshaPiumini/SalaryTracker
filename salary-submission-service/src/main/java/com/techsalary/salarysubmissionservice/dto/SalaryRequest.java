package com.techsalary.salarysubmissionservice.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SalaryRequest {

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "Company is required")
    private String company;

    @NotBlank(message = "Role is required")
    private String role;

    @NotBlank(message = "Level is required")
    private String level;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be positive")
    private Double salary;

    @NotBlank(message = "Currency is required")
    private String currency;

    @NotNull(message = "Years of experience is required")
    @Min(value = 0, message = "Experience cannot be negative")
    private Integer yearsExperience;

    private Boolean anonymize = false;
}