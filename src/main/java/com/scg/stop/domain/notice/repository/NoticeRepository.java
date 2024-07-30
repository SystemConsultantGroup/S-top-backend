package com.scg.stop.domain.notice.repository;

import com.scg.stop.domain.notice.domain.Notice;
import com.scg.stop.domain.notice.dto.response.NoticeListElementResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("SELECT n FROM Notice n " +
            "WHERE (:title IS NULL OR n.title LIKE %:title%)")
    Page<NoticeListElementResponse> findNotices(@Param("title") String title, Pageable pageable);
}