package com.scg.stop.event.controller;

import static com.scg.stop.user.domain.AccessType.ADMIN;

import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.event.dto.request.EventPeriodRequest;
import com.scg.stop.event.dto.response.EventPeriodResponse;
import com.scg.stop.event.service.EventPeriodService;
import com.scg.stop.user.domain.User;
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

    @PostMapping("/eventPeriods")
    public ResponseEntity<EventPeriodResponse> createEventPeriod(
            @RequestBody @Valid EventPeriodRequest eventPeriodRequest,
            @AuthUser(accessType = ADMIN) User user
    ) {
        EventPeriodResponse eventPeriodResponse = eventPeriodService.createEventPeriod(eventPeriodRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(eventPeriodResponse);
    }

    @GetMapping("/eventPeriod")
    public ResponseEntity<EventPeriodResponse> getEventPeriod(@AuthUser(accessType = ADMIN) User user) {
        EventPeriodResponse eventPeriodResponse = eventPeriodService.getEventPeriod();
        return ResponseEntity.status(HttpStatus.OK).body(eventPeriodResponse);
    }

    @PutMapping("/eventPeriod")
    public ResponseEntity<EventPeriodResponse> updateEventPeriod(
            @RequestBody @Valid EventPeriodRequest eventPeriodRequest,
            @AuthUser(accessType = ADMIN) User user
    ) {
        EventPeriodResponse eventPeriodResponse = eventPeriodService.updateEventPeriod(eventPeriodRequest);
        return ResponseEntity.status(HttpStatus.OK).body(eventPeriodResponse);
    }
}
