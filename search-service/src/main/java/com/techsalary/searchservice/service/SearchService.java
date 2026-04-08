package com.techsalary.searchservice.service;

import com.techsalary.searchservice.dto.SalarySearchResponse;
import com.techsalary.searchservice.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository repository;

    public List<SalarySearchResponse> search(
            String country, String company,
            String role, String level) {

        return repository.search(country, company, role, level)
                .stream()
                .map(SalarySearchResponse::from)  // applies anonymize rule
                .collect(Collectors.toList());
    }
}