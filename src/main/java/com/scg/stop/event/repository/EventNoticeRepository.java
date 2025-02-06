package com.scg.stop.event.repository;

import com.scg.stop.event.domain.EventNotice;
import com.scg.stop.event.dto.response.EventNoticeListElementResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventNoticeRepository extends JpaRepository<EventNotice, Long> {

    @Query("SELECT n FROM EventNotice n " +
            "WHERE (:searchScope IS NULL OR " +
            "(:searchScope = 'content' AND (:searchTerm IS NULL OR n.content LIKE %:searchTerm%)) " +
            "OR (:searchScope = 'title' AND (:searchTerm IS NULL OR n.title LIKE %:searchTerm%)) " +
            "OR (:searchScope = 'both' AND (:searchTerm IS NULL OR n.title LIKE %:searchTerm% OR n.content LIKE %:searchTerm%))) " +
            "AND n.fixed = false")
    Page<EventNoticeListElementResponse> findNonFixedEventNotices(
            @Param("searchTerm") String searchTerm,
            @Param("searchScope") String searchScope,
            Pageable pageable);


    @Query("SELECT n FROM EventNotice n " +
            "WHERE (:searchScope IS NULL OR " +
            "(:searchScope = 'content' AND (:searchTerm IS NULL OR n.content LIKE %:searchTerm%)) " +
            "OR (:searchScope = 'title' AND (:searchTerm IS NULL OR n.title LIKE %:searchTerm%)) " +
            "OR (:searchScope = 'both' AND (:searchTerm IS NULL OR n.title LIKE %:searchTerm% OR n.content LIKE %:searchTerm%))) " +
            "AND n.fixed = true")
    List<EventNoticeListElementResponse> findFixedEventNotices(
            @Param("searchTerm") String searchTerm,
            @Param("searchScope") String searchScope,
            Sort sort);
}
