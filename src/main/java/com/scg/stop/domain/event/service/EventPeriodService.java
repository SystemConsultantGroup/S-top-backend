package com.scg.stop.domain.event.service;

import com.scg.stop.domain.event.domain.EventPeriod;
import com.scg.stop.domain.event.dto.EventPeriodRequest;
import com.scg.stop.domain.event.dto.EventPeriodResponse;
import com.scg.stop.domain.event.repository.EventPeriodRepository;
import com.scg.stop.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventPeriodService {

    private final EventPeriodRepository eventPeriodRepository;

    public EventPeriodResponse createEventPeriod(EventPeriodRequest createEventPeriodRequest) {
        if (eventPeriodRepository.existsByYear(createEventPeriodRequest.getYear())) {
            throw new BadRequestException(createEventPeriodRequest.getYear() + "년의 행사 기간이 이미 존재합니다.");
        }
        EventPeriod newEventPeriod = eventPeriodRepository.save(EventPeriod.from(createEventPeriodRequest));
        return EventPeriodResponse.from(newEventPeriod);
    }
}
