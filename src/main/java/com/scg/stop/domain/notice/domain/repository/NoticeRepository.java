package com.scg.stop.domain.notice.domain.repository;

import com.scg.stop.domain.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
