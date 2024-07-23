package com.scg.stop.domain.event.controller;

import com.scg.stop.domain.event.dto.EventPeriodRequest;
import com.scg.stop.domain.event.dto.EventPeriodResponse;
import com.scg.stop.domain.event.service.EventPeriodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/eventPeriods")
public class EventPeriodController {

    private final EventPeriodService eventPeriodService;

    // TODO : ADMIN 권한 확인
    @PostMapping
    public ResponseEntity<EventPeriodResponse> createEventPeriod(@RequestBody @Valid EventPeriodRequest createEventPeriodRequest) {
        EventPeriodResponse eventPeriodResponse = eventPeriodService.createEventPeriod(createEventPeriodRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventPeriodResponse);
    }
}
