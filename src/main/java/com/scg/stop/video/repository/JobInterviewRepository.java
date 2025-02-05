package com.scg.stop.video.repository;

import com.scg.stop.video.domain.JobInterviewCategory;
import com.scg.stop.video.domain.JobInterview;
import com.scg.stop.video.dto.response.JobInterviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JobInterviewRepository extends JpaRepository<JobInterview, Long> {

    @Query("SELECT j FROM JobInterview j " +
            "WHERE (:year IS NULL OR j.year = :year) " +
            "AND (:category IS NULL OR j.category = :category) " +
            "AND (:title IS NULL OR j.title LIKE %:title%)" +
            "ORDER BY j.year DESC")
    Page<JobInterview> findJobInterviews(@Param("year") Integer year,
                                                 @Param("category") JobInterviewCategory category,
                                                 @Param("title") String title,
                                                 Pageable pageable);
}