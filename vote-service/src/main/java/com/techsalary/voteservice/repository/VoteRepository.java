package com.techsalary.voteservice.repository;

import com.techsalary.voteservice.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findBySubmissionId(Long submissionId);
    boolean existsBySubmissionIdAndUserId(Long submissionId, Long userId);
    long countBySubmissionIdAndUpvote(Long submissionId, Boolean upvote);
}