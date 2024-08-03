package com.scg.stop.domain.event.controller;

import com.scg.stop.domain.event.dto.request.EventPeriodRequest;
import com.scg.stop.domain.event.dto.response.EventPeriodResponse;
import com.scg.stop.domain.event.service.EventPeriodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventPeriodController {

    private final EventPeriodService eventPeriodService;

    // TODO : ADMIN 권한 확인
    @PostMapping("/eventPeriods")
    public ResponseEntity<EventPeriodResponse> createEventPeriod(@RequestBody @Valid EventPeriodRequest eventPeriodRequest) {
        EventPeriodResponse eventPeriodResponse = eventPeriodService.createEventPeriod(eventPeriodRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventPeriodResponse);
    }

    // TODO : ADMIN 권한 확인
    @GetMapping("/eventPeriod")
    public ResponseEntity<EventPeriodResponse> getEventPeriod() {
        EventPeriodResponse eventPeriodResponse = eventPeriodService.getEventPeriod();
        return ResponseEntity.status(HttpStatus.OK).body(eventPeriodResponse);
    }

    @PutMapping("/eventPeriod")
    public ResponseEntity<EventPeriodResponse> updateEventPeriod(@RequestBody @Valid EventPeriodRequest eventPeriodRequest) {
        EventPeriodResponse eventPeriodResponse = eventPeriodService.updateEventPeriod(eventPeriodRequest);
        return ResponseEntity.status(HttpStatus.OK).body(eventPeriodResponse);
    }
}
