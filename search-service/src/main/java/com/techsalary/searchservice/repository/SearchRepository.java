package com.techsalary.searchservice.repository;

import com.techsalary.searchservice.model.SalarySubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface SearchRepository extends JpaRepository<SalarySubmission, Long> {

    @Query(value = """
        SELECT * FROM salary.submissions
        WHERE status = 'APPROVED'
        AND (:country IS NULL OR LOWER(country) LIKE LOWER(CONCAT('%', CAST(:country AS TEXT), '%')))
        AND (:company IS NULL OR LOWER(company) LIKE LOWER(CONCAT('%', CAST(:company AS TEXT), '%')))
        AND (:role    IS NULL OR LOWER(role)    LIKE LOWER(CONCAT('%', CAST(:role    AS TEXT), '%')))
        AND (:level   IS NULL OR LOWER(level)   LIKE LOWER(CONCAT('%', CAST(:level   AS TEXT), '%')))
    """, nativeQuery = true)
    List<SalarySubmission> search(
            @Param("country") String country,
            @Param("company") String company,
            @Param("role")    String role,
            @Param("level")   String level
    );
}