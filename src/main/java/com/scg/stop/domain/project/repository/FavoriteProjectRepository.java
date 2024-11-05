package com.scg.stop.domain.project.repository;

import com.scg.stop.project.domain.FavoriteProject;
import com.scg.stop.project.domain.Project;
import com.scg.stop.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteProjectRepository extends JpaRepository<FavoriteProject, Long> {
    @Query("SELECT f.project FROM FavoriteProject f WHERE f.user = :user")
    List<Project> findAllByUser(@Param("user") User user);
}
