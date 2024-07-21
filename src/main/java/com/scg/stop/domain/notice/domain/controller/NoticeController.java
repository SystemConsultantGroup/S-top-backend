package com.scg.stop.domain.notice.domain.controller;

import com.scg.stop.domain.notice.domain.dto.NoticeDto;
import com.scg.stop.domain.notice.domain.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/notices")
@RestController
public class NoticeController {

    private final NoticeService noticeService;

    // Create a new notice
    @PostMapping
    public ResponseEntity<Long> createNotice(@RequestBody @Valid NoticeDto.Request dto) {
        Long noticeId = noticeService.createNotice(dto);
        return new ResponseEntity<>(noticeId, HttpStatus.CREATED);
    }

    // TODO: Implement GetNoticeList

    // Get a corresponding notice
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDto.Response> getNotice(@PathVariable Long noticeId) {
        return ResponseEntity.ok(noticeService.getNotice(noticeId));
    }

    // Update a corresponding notice
    @PutMapping("/{noticeId}")
    public ResponseEntity<Long> updateNotice(@PathVariable Long noticeId, @RequestBody @Valid NoticeDto.Request dto) {
        noticeService.updateNotice(noticeId, dto);
        return ResponseEntity.ok(noticeId);
    }

    // Delete a corresponding notice
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Long> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.noContent().build();
    }
}
