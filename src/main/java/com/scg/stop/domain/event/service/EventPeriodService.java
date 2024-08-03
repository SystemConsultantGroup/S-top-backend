package com.scg.stop.domain.event.service;

import static com.scg.stop.global.exception.ExceptionCode.DUPLICATED_YEAR;
import static com.scg.stop.global.exception.ExceptionCode.INVALID_EVENT_PERIOD;
import static com.scg.stop.global.exception.ExceptionCode.NOT_FOUND_EVENT_PERIOD;

import com.scg.stop.domain.event.domain.EventPeriod;
import com.scg.stop.domain.event.dto.request.CreateEventPeriodRequest;
import com.scg.stop.domain.event.dto.response.EventPeriodResponse;
import com.scg.stop.domain.event.repository.EventPeriodRepository;
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

    public EventPeriodResponse createEventPeriod(CreateEventPeriodRequest request) {
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
    public List<EventPeriodResponse> getEventPeriods() {
        List<EventPeriod> eventPeriods = eventPeriodRepository.findAll();
        List<EventPeriodResponse> eventPeriodResponses = eventPeriods.stream()
                .map(EventPeriodResponse::from)
                .collect(Collectors.toList());
        return eventPeriodResponses;
    }

    public void deleteEventPeriod(Long eventPeriodId) {
        EventPeriod eventPeriod = eventPeriodRepository.findById(eventPeriodId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_EVENT_PERIOD));
        eventPeriodRepository.delete(eventPeriod);
    }
}
