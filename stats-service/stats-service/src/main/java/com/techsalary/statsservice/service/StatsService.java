package com.techsalary.statsservice.service;

import com.techsalary.statsservice.dto.StatsResponse;
import com.techsalary.statsservice.repository.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository repository;

    public StatsResponse getStats(String country, String role, String level) {
        List<Object[]> results = repository.getStats(country, role, level);

        // If no approved salaries match the filter
        if (results.isEmpty() || results.get(0)[0] == null) {
            return new StatsResponse(country, role, level, 0.0, 0.0, 0.0, 0.0, 0L);
        }

        Object[] row = results.get(0);

        // PostgreSQL returns numeric types — safely cast each column
        Double average  = row[0] != null ? ((Number) row[0]).doubleValue() : 0.0;
        Double median   = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
        Double p25      = row[2] != null ? ((Number) row[2]).doubleValue() : 0.0;
        Double p75      = row[3] != null ? ((Number) row[3]).doubleValue() : 0.0;
        Long   total    = row[4] != null ? ((Number) row[4]).longValue()   : 0L;

        return new StatsResponse(country, role, level, average, median, p25, p75, total);
    }
}