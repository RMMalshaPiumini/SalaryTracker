package com.techsalary.statsservice.controller;

import com.techsalary.statsservice.dto.StatsResponse;
import com.techsalary.statsservice.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping
    public ResponseEntity<StatsResponse> getStats(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String level) {

        return ResponseEntity.ok(statsService.getStats(country, role, level));
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}