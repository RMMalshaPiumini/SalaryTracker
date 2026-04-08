package com.techsalary.statsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsResponse {
    private String country;
    private String role;
    private String level;
    private Double averageSalary;
    private Double medianSalary;
    private Double percentile25;
    private Double percentile75;
    private Long totalEntries;
}