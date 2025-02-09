package com.scg.stop.project.repository;


import com.scg.stop.project.domain.Project;
import com.scg.stop.project.domain.ProjectCategory;
import com.scg.stop.project.domain.ProjectType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
//    @EntityGraph(attributePaths = {"thumbnail", "members"})
    @Query("SELECT p FROM Project p " +
            "WHERE (:title IS NULL OR p.name LIKE %:title%) " +
            "AND (:year IS NULL OR p.year IN :year) " +
            "AND (:category IS NULL OR p.category IN :category)" +
            "AND (:type IS NULL OR p.type IN :type)")
    Page<Project> findProjects(
            @Param("title") String title,
            @Param("year") List<Integer> year,
            @Param("category") List<ProjectCategory> category,
            @Param("type") List<ProjectType> type,
            Pageable pageable
    );

    @Query(value = "SELECT p.* FROM project p " +
            "WHERE (:title IS NULL OR p.name LIKE %:title%) " +
            "AND (:year IS NULL OR p.year IN :year) " +
            "AND (:category IS NULL OR p.category IN :category) " +
            "AND (:type IS NULL OR p.type IN :type) " +
            "ORDER BY MOD(UNIX_TIMESTAMP(NOW()) DIV 3600, p.id)",
            nativeQuery = true)
    Page<Project> findEventProjects(
            @Param("title") String title,
            @Param("year") List<Integer> year,
            @Param("category") List<ProjectCategory> category,
            @Param("type") List<ProjectType> type,
            Pageable pageable
    );

    @Query("SELECT p FROM Project p " +
            "WHERE p.year = :year " +
            "AND p.awardStatus != com.scg.stop.project.domain.AwardStatus.NONE")
   Page<Project> findAwardProjects(
           @Param("year") Integer year,
           Pageable pageable
   );
}