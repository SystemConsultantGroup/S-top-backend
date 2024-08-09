package com.scg.stop.domain.project.repository;

import com.scg.stop.domain.project.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
