package com.scg.stop.video.repository;

import com.scg.stop.video.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
    Optional<Quiz> findByTalkId(Long talkId);
}
