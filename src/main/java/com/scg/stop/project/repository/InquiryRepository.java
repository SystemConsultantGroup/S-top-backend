package com.scg.stop.project.repository;

import com.scg.stop.project.domain.Inquiry;
import com.scg.stop.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    @Query("SELECT i FROM Inquiry i WHERE :title IS NULL OR i.title LIKE %:title%")
    Page<Inquiry> findInquiries(@Param("title") String title, Pageable pageable);

    List<Inquiry> findByUser(User user);
}
