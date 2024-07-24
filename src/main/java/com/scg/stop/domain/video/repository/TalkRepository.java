package com.scg.stop.domain.video.repository;

import com.scg.stop.domain.video.domain.Talk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalkRepository extends JpaRepository<Talk, Long> {
}
