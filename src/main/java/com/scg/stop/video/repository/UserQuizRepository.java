package com.scg.stop.video.repository;

import com.scg.stop.user.domain.User;
import com.scg.stop.video.domain.Quiz;
import com.scg.stop.video.domain.UserQuiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserQuizRepository extends JpaRepository<UserQuiz, Long> {
    UserQuiz findByUserAndQuiz(User user, Quiz quiz);
}
