package com.scg.stop.domain.event.service;

import com.scg.stop.domain.event.domain.EventPeriod;
import com.scg.stop.domain.event.dto.EventPeriodDto;
import com.scg.stop.domain.event.repository.EventPeriodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventPeriodService {

    private final EventPeriodRepository eventPeriodRepository;

    public EventPeriodDto.Response createEventPeriod(EventPeriodDto.Request requestDto) {
        EventPeriod newEventPeriod = eventPeriodRepository.save(requestDto.toEntity());
        return new EventPeriodDto.Response(newEventPeriod);
    }
}
