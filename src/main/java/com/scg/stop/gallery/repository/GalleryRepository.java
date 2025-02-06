package com.scg.stop.gallery.repository;

import com.scg.stop.gallery.domain.Gallery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {

    @Query("SELECT g FROM Gallery g " +
            "WHERE (:year IS NULL OR g.year = :year) " +
            "AND (:month IS NULL OR g.month = :month)" +
            "ORDER BY g.year DESC, g.month DESC")
    Page<Gallery> findGalleries(
            @Param("year") Integer year,
            @Param("month") Integer month,
            Pageable pageable
    );
}
