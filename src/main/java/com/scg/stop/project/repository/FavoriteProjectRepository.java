package com.scg.stop.project.repository;

import com.scg.stop.project.domain.FavoriteProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteProjectRepository extends JpaRepository<FavoriteProject, Long> {
    Optional<FavoriteProject> findByProjectIdAndUserId(Long projectId, Long userId);
}
