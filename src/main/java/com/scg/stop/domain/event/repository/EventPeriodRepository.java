package com.scg.stop.domain.event.repository;

import com.scg.stop.domain.event.domain.EventPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventPeriodRepository extends JpaRepository<EventPeriod, Long> {
    Optional<EventPeriod> findByYear(Integer year);
}
