package com.scg.stop.event.repository;

import com.scg.stop.event.domain.EventPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventPeriodRepository extends JpaRepository<EventPeriod, Long> {

    Boolean existsByYear(Integer year);
    EventPeriod findByYear(Integer year);
}
