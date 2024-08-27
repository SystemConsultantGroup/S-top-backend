package com.scg.stop.video.repository;

import com.scg.stop.user.domain.User;
import com.scg.stop.video.domain.Quiz;
import com.scg.stop.video.domain.UserQuiz;
import com.scg.stop.video.dto.response.UserQuizResultResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserQuizRepository extends JpaRepository<UserQuiz, Long> {
    UserQuiz findByUserAndQuiz(User user, Quiz quiz);


    @Query("SELECT new com.scg.stop.video.dto.response.UserQuizResultResponse(u.id, u.name, u.phone, u.email, SUM(CASE WHEN uq.isSuccess = true THEN 1 ELSE 0 END)) " +
            "FROM User u " +
            "LEFT JOIN UserQuiz uq ON u.id = uq.user.id " +
            "WHERE (:year IS NULL OR YEAR(uq.createdAt) = :year) " +
            "GROUP BY u.id, u.name, u.phone, u.email " +
            "ORDER BY SUM(CASE WHEN uq.isSuccess = true THEN 1 ELSE 0 END) DESC")
    Page<UserQuizResultResponse> findUserQuizResults(@Param("year") Integer year, Pageable pageable);

    @Query("SELECT new com.scg.stop.video.dto.response.UserQuizResultResponse(u.id, u.name, u.phone, u.email, SUM(CASE WHEN uq.isSuccess = true THEN 1 ELSE 0 END)) " +
            "FROM User u " +
            "LEFT JOIN UserQuiz uq ON u.id = uq.user.id " +
            "WHERE (:year IS NULL OR YEAR(uq.createdAt) = :year) " +
            "GROUP BY u.id, u.name, u.phone, u.email " +
            "ORDER BY SUM(CASE WHEN uq.isSuccess = true THEN 1 ELSE 0 END) DESC")
    List<UserQuizResultResponse> findAllByYear(@Param("year") Integer year);

}
