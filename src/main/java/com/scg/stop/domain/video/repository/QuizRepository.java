package com.scg.stop.domain.video.repository;

import com.scg.stop.domain.video.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
