package com.scg.stop.domain.event.service;

import com.scg.stop.domain.event.domain.EventPeriod;
import com.scg.stop.domain.event.dto.EventPeriodRequest;
import com.scg.stop.domain.event.dto.EventPeriodResponse;
import com.scg.stop.domain.event.repository.EventPeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventPeriodService {

    private final EventPeriodRepository eventPeriodRepository;

    public EventPeriodResponse createEventPeriod(EventPeriodRequest createEventPeriodRequest) {
        // 연도 중복 판단
        if (eventPeriodRepository.existsByYear(createEventPeriodRequest.getYear())) {
            throw new IllegalStateException("이미 존재하는 연도입니다");
        }

        // Request DTO -> Entity
        EventPeriod eventPeriod = EventPeriod.from(createEventPeriodRequest);

        // Save
        EventPeriod newEventPeriod = eventPeriodRepository.save(eventPeriod);

        // Response DTO -> Entity
        EventPeriodResponse eventPeriodResponse = EventPeriodResponse.from(newEventPeriod);

        return eventPeriodResponse;
    }
}
