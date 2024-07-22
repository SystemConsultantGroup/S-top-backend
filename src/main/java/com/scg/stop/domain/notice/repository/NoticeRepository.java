package com.scg.stop.domain.notice.repository;

import com.scg.stop.domain.notice.domain.Notice;
import com.scg.stop.domain.notice.dto.NoticeDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // TODO: update hitCount

    @Transactional
    @Modifying
    @Query("UPDATE Notice n SET n.title = :#{#dto.title}, n.content = :#{#dto.content}, n.fixed = :#{#dto.fixed} WHERE n.id = :id")
    int updateNotice(@Param("id") Long id, @Param("dto") NoticeDto.Request dto);

    @Query("SELECT n FROM Notice n " +
            "WHERE (:title IS NULL OR n.title = :title)")
    Page<NoticeDto.Response> findNotices(@Param("title") String title, Pageable pageable);
}
