package com.scg.stop.domain.event.service;

import static com.scg.stop.global.exception.ExceptionCode.DUPLICATED_YEAR;
import static org.hamcrest.Matchers.any;

import com.scg.stop.domain.event.domain.EventPeriod;
import com.scg.stop.domain.event.dto.request.CreateEventPeriodRequest;
import com.scg.stop.domain.event.dto.response.EventPeriodResponse;
import com.scg.stop.domain.event.repository.EventPeriodRepository;
import com.scg.stop.global.exception.BadRequestException;
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

    public EventPeriodResponse createEventPeriod(CreateEventPeriodRequest createEventPeriodRequest) {
        if (eventPeriodRepository.existsByYear(createEventPeriodRequest.getYear())) {
            throw new BadRequestException(DUPLICATED_YEAR);
        }
        EventPeriod newEventPeriod = eventPeriodRepository.save(EventPeriod.from(createEventPeriodRequest));
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

    public void deleteEventPeriod(Long id) {
    }
}
