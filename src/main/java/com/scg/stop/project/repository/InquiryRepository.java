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
    @Query("SELECT i FROM Inquiry i " +
            "WHERE (:searchScope IS NULL OR " +
            "(:searchScope = 'content' AND (:searchTerm IS NULL OR i.content LIKE %:searchTerm%)) " +
            "OR (:searchScope = 'title' AND (:searchTerm IS NULL OR i.title LIKE %:searchTerm%)) " +
            "OR (:searchScope = 'author' AND (:searchTerm IS NULL OR i.user.name LIKE %:searchTerm%)) " +
            "OR (:searchScope = 'both' AND (:searchTerm IS NULL OR i.title LIKE %:searchTerm% OR i.content LIKE %:searchTerm%)))")
    Page<Inquiry> findInquiries(
            @Param("searchTerm") String searchTerm,
            @Param("searchScope") String searchScope,
            Pageable pageable);

    List<Inquiry> findByUser(User user);
}
