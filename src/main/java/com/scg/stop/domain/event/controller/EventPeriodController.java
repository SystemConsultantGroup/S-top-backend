package com.scg.stop.domain.event.controller;

import com.scg.stop.domain.event.dto.request.CreateEventPeriodRequest;
import com.scg.stop.domain.event.dto.response.EventPeriodResponse;
import com.scg.stop.domain.event.service.EventPeriodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventPeriodController {

    private final EventPeriodService eventPeriodService;

    // TODO : ADMIN 권한 확인
    @PostMapping
    @RequestMapping("/eventPeriods")
    public ResponseEntity<EventPeriodResponse> createEventPeriod(@RequestBody @Valid CreateEventPeriodRequest createEventPeriodRequest) {
        EventPeriodResponse eventPeriodResponse = eventPeriodService.createEventPeriod(createEventPeriodRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventPeriodResponse);
    }

    // TODO : ADMIN 권한 확인
    @GetMapping
    @RequestMapping("/eventPeriod")
    public ResponseEntity<EventPeriodResponse> getEventPeriod() {
        EventPeriodResponse eventPeriodResponse = eventPeriodService.getEventPeriod();
        return ResponseEntity.status(HttpStatus.OK).body(eventPeriodResponse);
    }

    @DeleteMapping("/{eventPeriodId}")
    public ResponseEntity<Void> deleteEventPeriod(@PathVariable("eventPeriodId") Long eventPeriodId) {
        eventPeriodService.deleteEventPeriod(eventPeriodId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
