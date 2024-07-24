package com.scg.stop.domain.project.repository;

import com.scg.stop.domain.project.domain.Project;
import com.scg.stop.domain.project.domain.ProjectCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectRepository extends JpaRepository<Project, Long> {
//    @EntityGraph(attributePaths = {"thumbnail", "members"})
    @Query("SELECT p FROM Project p " +
            "WHERE (:title IS NULL OR p.name LIKE %:title%) " +
            "AND (:year IS NULL OR p.year = :year) " +
            "AND (:category IS NULL OR p.category = :category)")
    Page<Project> findProjects(
            @Param("title") String title,
            @Param("year") Integer year,
            @Param("category") ProjectCategory category,
            Pageable pageable
    );
}
