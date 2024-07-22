package com.scg.stop.domain.video.repository;

import com.scg.stop.domain.video.domain.Category;
import com.scg.stop.domain.video.domain.JobInterview;
import com.scg.stop.domain.video.dto.JobInterviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface JobInterviewRepository extends JpaRepository<JobInterview, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE JobInterview j SET j.title = :#{#dto.title}, j.youtubeId = :#{#dto.youtubeId}, j.year = :#{#dto.year} WHERE j.id = :id")
    int updateJobInterview(@Param("id") Long id, @Param("dto") JobInterviewDto.Request dto);

    @Query("SELECT j FROM JobInterview j " +
            "WHERE (:year IS NULL OR j.year = :year) " +
            "AND (:category IS NULL OR j.category = :category) " +
            "AND (:title IS NULL OR j.title LIKE %:title%)")
    Page<JobInterviewDto.Response> findJobInterviews(@Param("year") Integer year,
                                                     @Param("category")Category category,
                                                     @Param("title") String title,
                                                     Pageable pageable);

}
