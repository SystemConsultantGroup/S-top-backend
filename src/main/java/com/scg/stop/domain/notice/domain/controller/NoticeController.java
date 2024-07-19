package com.scg.stop.domain.notice.domain.controller;

import com.scg.stop.domain.notice.domain.dto.NoticeDto;
import com.scg.stop.domain.notice.domain.service.NoticeService;
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
    public ResponseEntity<Long> createNotice(@RequestBody NoticeDto.Request dto) {
        Long noticeId = noticeService.createNotice(dto);
        return new ResponseEntity<>(noticeId, HttpStatus.CREATED);
    }

    // TODO: Implement GetNoticeList

    // Get a corresponding notice
    @GetMapping("/{NoticeId}")
    public ResponseEntity<NoticeDto.Response> getNotice(@PathVariable Long NoticeId) {
        return ResponseEntity.ok(noticeService.getNotice(NoticeId));
    }

    // Update a corresponding notice
    @PutMapping("/{NoticeId}")
    public ResponseEntity<Long> updateNotice(@PathVariable Long NoticeId, @RequestBody NoticeDto.Request dto) {
        noticeService.updateNotice(NoticeId, dto);
        return ResponseEntity.ok(NoticeId);
    }

    // Delete a corresponding notice
    @DeleteMapping("/{NoticeId}")
    public ResponseEntity<Long> deleteNotice(@PathVariable Long NoticeId) {
        noticeService.deleteNotice(NoticeId);
        return ResponseEntity.ok(NoticeId);
    }
}
