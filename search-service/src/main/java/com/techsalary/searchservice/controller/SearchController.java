package com.techsalary.searchservice.controller;

import com.techsalary.searchservice.dto.SalarySearchResponse;
import com.techsalary.searchservice.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    // All parameters are optional — mix and match freely
    @GetMapping
    public ResponseEntity<List<SalarySearchResponse>> search(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String level) {

        return ResponseEntity.ok(
                searchService.search(country, company, role, level)
        );
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}