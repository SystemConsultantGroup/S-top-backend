package com.scg.stop.project.repository;

import com.scg.stop.project.domain.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    @Query("SELECT i FROM Inquiry i WHERE :title IS NULL OR i.title LIKE %:title%")
    Page<Inquiry> findInquiries(@Param("title") String title, Pageable pageable);
}
