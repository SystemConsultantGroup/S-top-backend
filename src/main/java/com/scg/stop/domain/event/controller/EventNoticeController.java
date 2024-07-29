package com.scg.stop.domain.event.controller;


import com.scg.stop.domain.event.dto.request.EventNoticeRequest;
import com.scg.stop.domain.event.dto.response.EventNoticeListElementResponse;
import com.scg.stop.domain.event.dto.response.EventNoticeResponse;
import com.scg.stop.domain.event.service.EventNoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/eventNotices")
@RestController
// TODO: Admin check
public class EventNoticeController {

    private final EventNoticeService eventNoticeService;

    // Create a new eventNotice
    @PostMapping
    public ResponseEntity<EventNoticeResponse> createEventNotice(@RequestBody @Valid EventNoticeRequest createEventNoticeDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(eventNoticeService.createEventNotice(createEventNoticeDto));
    }

    // Get a list of eventNotices
    @GetMapping
    public ResponseEntity<Page<EventNoticeListElementResponse>> getEventNoticeList(
            @RequestParam(value = "title", required = false) String title,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<EventNoticeListElementResponse> eventNoticeList = eventNoticeService.getEventNoticeList(title, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(eventNoticeList);
    }

    // Get a corresponding eventNotice
    @GetMapping("/{eventNoticeId}")
    public ResponseEntity<EventNoticeResponse> getEventNotice(@PathVariable("eventNoticeId") Long eventNoticeId) {
        EventNoticeResponse eventNotice = eventNoticeService.getEventNotice(eventNoticeId);
        return ResponseEntity.status(HttpStatus.OK).body(eventNotice);
    }

    // Update a corresponding eventNotice
    @PutMapping("/{eventNoticeId}")
    public ResponseEntity<EventNoticeResponse> updateEventNotice(@PathVariable("eventNoticeId") Long eventNoticeId, @RequestBody @Valid EventNoticeRequest updateEventNoticeDto) {
        return ResponseEntity.status(HttpStatus.OK).body(eventNoticeService.updateEventNotice(eventNoticeId, updateEventNoticeDto));
    }

    // Delete a corresponding eventNotice
    @DeleteMapping("/{eventNoticeId}")
    public ResponseEntity<Long> deleteEventNotice(@PathVariable("eventNoticeId") Long eventNoticeId) {
        eventNoticeService.deleteEventNotice(eventNoticeId);
        return ResponseEntity.noContent().build();
    }
}
