package com.scg.stop.notice.repository;

import com.scg.stop.notice.domain.Notice;
import com.scg.stop.notice.dto.response.NoticeListElementResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("SELECT n FROM Notice n " +
            "WHERE (:searchScope IS NULL OR " +
            "(:searchScope = 'content' AND (:searchTerm IS NULL OR n.content LIKE %:searchTerm%)) " +
            "OR (:searchScope = 'title' AND (:searchTerm IS NULL OR n.title LIKE %:searchTerm%)) " +
            "OR (:searchScope = 'both' AND (:searchTerm IS NULL OR n.title LIKE %:searchTerm% OR n.content LIKE %:searchTerm%))) " +
            "AND n.fixed = false")
    Page<NoticeListElementResponse> findNonFixedNotices(
            @Param("searchTerm") String searchTerm,
            @Param("searchScope") String searchScope,
            Pageable pageable);

    @Query("SELECT n FROM Notice n " +
            "WHERE (:searchScope IS NULL OR " +
            "(:searchScope = 'content' AND (:searchTerm IS NULL OR n.content LIKE %:searchTerm%)) " +
            "OR (:searchScope = 'title' AND (:searchTerm IS NULL OR n.title LIKE %:searchTerm%)) " +
            "OR (:searchScope = 'both' AND (:searchTerm IS NULL OR n.title LIKE %:searchTerm% OR n.content LIKE %:searchTerm%))) " +
            "AND n.fixed = true")
    List<NoticeListElementResponse> findFixedNotices(
            @Param("searchTerm") String searchTerm,
            @Param("searchScope") String searchScope,
            Sort sort);
}
