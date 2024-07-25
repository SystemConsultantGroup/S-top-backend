package com.scg.stop.domain.gallery.repository;

import com.scg.stop.domain.gallery.domain.Gallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryRepository extends JpaRepository<Gallery, Long> {
}
