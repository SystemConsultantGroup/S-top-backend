package com.scg.stop.domain.gallery.repository;

import com.scg.stop.domain.gallery.domain.Gallery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {

    @Query("SELECT g FROM Gallery g " +
            "WHERE (:year IS NULL OR g.year = :year) " +
            "AND (:month IS NULL OR g.month = :month)")
    Page<Gallery> findGalleries(
            @Param("year") Integer year,
            @Param("month") Integer month,
            Pageable pageable
    );
}
