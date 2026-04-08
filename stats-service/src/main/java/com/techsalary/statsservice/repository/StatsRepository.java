package com.techsalary.statsservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import com.techsalary.statsservice.model.SalarySubmission;
import java.util.List;

public interface StatsRepository extends JpaRepository<SalarySubmission, Long> {

    // PostgreSQL's percentile_cont is perfect for median and percentiles
    @Query(value = """
        SELECT
            AVG(salary)                                    AS average,
            PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY salary) AS median,
            PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY salary) AS p25,
            PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY salary) AS p75,
            COUNT(*)                                       AS total
        FROM salary.submissions
        WHERE status = 'APPROVED'
        AND (:country IS NULL OR LOWER(country) LIKE LOWER(CONCAT('%', CAST(:country AS TEXT), '%')))
        AND (:role    IS NULL OR LOWER(role)    LIKE LOWER(CONCAT('%', CAST(:role    AS TEXT), '%')))
        AND (:level   IS NULL OR LOWER(level)   LIKE LOWER(CONCAT('%', CAST(:level   AS TEXT), '%')))
    """, nativeQuery = true)
    List<Object[]> getStats(
            @Param("country") String country,
            @Param("role")    String role,
            @Param("level")   String level
    );
}