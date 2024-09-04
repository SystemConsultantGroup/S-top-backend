package com.scg.stop.event.repository;

import com.scg.stop.event.domain.EventPeriod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventPeriodRepository extends JpaRepository<EventPeriod, Long> {

    Boolean existsByYear(Integer year);
    Optional<EventPeriod> findByYear(Integer year);
}
