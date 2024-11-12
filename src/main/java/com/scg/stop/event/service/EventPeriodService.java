package com.scg.stop.event.service;

import static com.scg.stop.global.exception.ExceptionCode.DUPLICATED_YEAR;
import static com.scg.stop.global.exception.ExceptionCode.INVALID_EVENT_PERIOD;
import static com.scg.stop.global.exception.ExceptionCode.NOT_FOUND_EVENT_PERIOD;

import com.scg.stop.event.domain.EventPeriod;
import com.scg.stop.event.dto.request.EventPeriodRequest;
import com.scg.stop.event.dto.response.EventPeriodResponse;
import com.scg.stop.event.repository.EventPeriodRepository;
import com.scg.stop.global.exception.BadRequestException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventPeriodService {

    private final EventPeriodRepository eventPeriodRepository;

    public EventPeriodResponse createEventPeriod(EventPeriodRequest request) {
        int currentYear = LocalDateTime.now().getYear();
        if (request.getStart().getYear() != currentYear || request.getEnd().getYear() != currentYear) {
            throw new BadRequestException(INVALID_EVENT_PERIOD);
        }
        if (eventPeriodRepository.existsByYear(currentYear)) {
            throw new BadRequestException(DUPLICATED_YEAR);
        }
        EventPeriod newEventPeriod = eventPeriodRepository.save(EventPeriod.of(currentYear, request.getStart(), request.getEnd()));
        return EventPeriodResponse.from(newEventPeriod);
    }

    @Transactional(readOnly = true)
    public EventPeriodResponse getEventPeriod() {
        int currentYear = LocalDateTime.now().getYear();
        EventPeriod eventPeriod = eventPeriodRepository.findByYear(currentYear)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_EVENT_PERIOD));
        return EventPeriodResponse.from(eventPeriod);
    }

    @Transactional(readOnly = true)
    public List<EventPeriodResponse> getEventPeriods() {
        List<EventPeriod> eventPeriods = eventPeriodRepository.findAll();
        return eventPeriods.stream()
                .map(EventPeriodResponse::from)
                .collect(Collectors.toList());
    }

    public EventPeriodResponse updateEventPeriod(EventPeriodRequest request) {
        int currentYear = LocalDateTime.now().getYear();
        EventPeriod currentEventPeriod = eventPeriodRepository.findByYear(currentYear)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_EVENT_PERIOD));
        currentEventPeriod.update(request.getStart(), request.getEnd());
        return EventPeriodResponse.from(currentEventPeriod);
    }
}
