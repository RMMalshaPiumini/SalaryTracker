package com.techsalary.voteservice.service;

import com.techsalary.voteservice.dto.VoteRequest;
import com.techsalary.voteservice.model.Vote;
import com.techsalary.voteservice.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoteService {

    private final VoteRepository voteRepository;
    private final RestTemplate restTemplate;

    @Value("${services.salary-url}")
    private String salaryServiceUrl;

    @Value("${voting.approval-threshold}")
    private int approvalThreshold;

    public Vote castVote(VoteRequest request, Long userId) {

        // Check if user already voted on this submission
        if (voteRepository.existsBySubmissionIdAndUserId(
                request.getSubmissionId(), userId)) {
            throw new RuntimeException("You have already voted on this submission");
        }

        // Record the vote
        Vote vote = new Vote();
        vote.setSubmissionId(request.getSubmissionId());
        vote.setUserId(userId);      // userId only, never email
        vote.setUpvote(request.getUpvote());
        Vote saved = voteRepository.save(vote);

        // Count total upvotes for this submission
        long upvoteCount = voteRepository.countBySubmissionIdAndUpvote(
                request.getSubmissionId(), true);

        log.info("Submission {} has {} upvotes (threshold: {})",
                request.getSubmissionId(), upvoteCount, approvalThreshold);

        // If threshold reached → call salary service to approve it
        if (upvoteCount >= approvalThreshold) {
            approveSubmission(request.getSubmissionId());
        }

        return saved;
    }

    private void approveSubmission(Long submissionId) {
        try {
            String url = salaryServiceUrl + "/salaries/" + submissionId + "/approve";
            restTemplate.put(url, null);
            log.info("Submission {} has been APPROVED!", submissionId);
        } catch (Exception e) {
            log.error("Failed to approve submission {}: {}", submissionId, e.getMessage());
        }
    }

    public long getUpvoteCount(Long submissionId) {
        return voteRepository.countBySubmissionIdAndUpvote(submissionId, true);
    }
}