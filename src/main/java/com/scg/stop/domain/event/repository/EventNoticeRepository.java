package com.scg.stop.domain.event.repository;

import com.scg.stop.domain.event.domain.EventNotice;
import com.scg.stop.domain.event.dto.response.EventNoticeListElementResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EventNoticeRepository extends JpaRepository<EventNotice, Long> {
    @Query("SELECT n FROM EventNotice n " +
            "WHERE (:title IS NULL OR n.title LIKE %:title%)")
    Page<EventNoticeListElementResponse> findNotices(@Param("title") String title, Pageable pageable);
}
