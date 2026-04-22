package com.techsalary.bffservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BffService {

    private final RestTemplate restTemplate;

    @Value("${services.identity-url}")
    private String identityUrl;

    @Value("${services.salary-url}")
    private String salaryUrl;

    @Value("${services.vote-url}")
    private String voteUrl;

    @Value("${services.search-url}")
    private String searchUrl;

    @Value("${services.stats-url}")
    private String statsUrl;

    // ─── AUTH ────────────────────────────────────────────────────────────────

    public ResponseEntity<?> signup(Map<String, Object> body) {
        return forward(HttpMethod.POST, identityUrl + "/auth/signup", body, null);
    }

    public ResponseEntity<?> login(Map<String, Object> body) {
        return forward(HttpMethod.POST, identityUrl + "/auth/login", body, null);
    }

    // ─── SALARY SUBMISSION ───────────────────────────────────────────────────

    public ResponseEntity<?> submitSalary(Map<String, Object> body) {
        // No auth required — anyone can submit
        return forward(HttpMethod.POST, salaryUrl + "/salaries", body, null);
    }

    public ResponseEntity<?> getPendingSalaries() {
        return forward(HttpMethod.GET, salaryUrl + "/salaries/pending", null, null);
    }

    // ─── VOTING (AUTH REQUIRED) ──────────────────────────────────────────────

    public ResponseEntity<?> castVote(Map<String, Object> body, String authHeader) {
        // BFF enforces auth before forwarding to vote-service
        if (!isAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Login required to vote"));
        }
        return forward(HttpMethod.POST, voteUrl + "/votes", body, authHeader);
    }

    // ─── SEARCH ──────────────────────────────────────────────────────────────

    public ResponseEntity<?> search(String country, String company,
                                    String role, String level) {
        String url = searchUrl + "/search?";
        if (country != null) url += "country=" + country + "&";
        if (company != null) url += "company=" + company + "&";
        if (role    != null) url += "role="    + role    + "&";
        if (level   != null) url += "level="   + level   + "&";
        return forward(HttpMethod.GET, url, null, null);
    }

    // ─── STATS ───────────────────────────────────────────────────────────────

    public ResponseEntity<?> getStats(String country, String role, String level) {
        String url = statsUrl + "/stats?";
        if (country != null) url += "country=" + country + "&";
        if (role    != null) url += "role="    + role    + "&";
        if (level   != null) url += "level="   + level   + "&";
        return forward(HttpMethod.GET, url, null, null);
    }

    // ─── HELPERS ─────────────────────────────────────────────────────────────

    // Check token validity by calling identity-service
    public boolean isAuthenticated(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            ResponseEntity<Map> response = restTemplate.exchange(
                    identityUrl + "/auth/validate",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    Map.class
            );
            return response.getBody() != null &&
                    Boolean.TRUE.equals(response.getBody().get("valid"));
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    // Generic HTTP forwarder
    public ResponseEntity<?> forward(HttpMethod method, String url,
                                      Object body, String authHeader) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            if (authHeader != null) {
                headers.set("Authorization", authHeader);
            }

            HttpEntity<Object> entity = new HttpEntity<>(body, headers);
            return restTemplate.exchange(url, method, entity, Object.class);

        } catch (HttpClientErrorException e) {
            // Forward the exact error from the downstream service
            return ResponseEntity.status(e.getStatusCode())
                    .body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Error forwarding to {}: {}", url, e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("error", "Service unavailable: " + e.getMessage()));
        }
    }

    public ResponseEntity<?> report(Map<String, Object> body, String authHeader) {
        if (!isAuthenticated(authHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Login required to report"));
        }
        return forward(HttpMethod.POST, voteUrl + "/votes/report", body, authHeader);
    }
}