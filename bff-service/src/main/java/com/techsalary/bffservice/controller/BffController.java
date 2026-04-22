package com.techsalary.bffservice.controller;

import com.techsalary.bffservice.service.BffService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")   // allows frontend to call BFF from browser
public class BffController {

    private final BffService bffService;

    // ─── AUTH ────────────────────────────────────────────────────────────────

    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, Object> body) {
        return bffService.signup(body);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody Map<String, Object> body) {
        return bffService.login(body);
    }

    // ─── SALARY ──────────────────────────────────────────────────────────────

    @PostMapping("/salaries")
    public ResponseEntity<?> submitSalary(@RequestBody Map<String, Object> body) {
        return bffService.submitSalary(body);
    }

    @GetMapping("/salaries/pending")
    public ResponseEntity<?> getPending() {
        return bffService.getPendingSalaries();
    }

    @PostMapping("/report")
    public ResponseEntity<?> report(
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        return bffService.report(body, authHeader);
    }

    // ─── VOTING (auth enforced inside BffService) ─────────────────────────────

    @PostMapping("/votes")
    public ResponseEntity<?> castVote(
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {
        return bffService.castVote(body, authHeader);
    }

    // ─── SEARCH ──────────────────────────────────────────────────────────────

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String company,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String level) {
        return bffService.search(country, company, role, level);
    }

    // ─── STATS ───────────────────────────────────────────────────────────────

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String level) {
        return bffService.getStats(country, role, level);
    }

    // ─── HEALTH ──────────────────────────────────────────────────────────────

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}