package com.scg.stop.domain.project.repository;

import com.scg.stop.domain.project.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository  extends JpaRepository<Likes, Long> {
    Optional<Likes> findByProjectIdAndUserId(Long projectId, Long userId);
}

