package com.scg.stop.domain.notice.controller;

import com.scg.stop.domain.notice.dto.NoticeDto;
import com.scg.stop.domain.notice.service.NoticeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Long> createNotice(@RequestBody @Valid NoticeDto.Request createNoticeDto) {
        Long noticeId = noticeService.createNotice(createNoticeDto);
        return new ResponseEntity<>(noticeId, HttpStatus.CREATED);
    }

    // Get a list of notices
    @GetMapping
    public ResponseEntity<Page<NoticeDto.Response>> getNoticeList(
            @RequestParam(value = "title", required = false) String title,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<NoticeDto.Response> noticeList = noticeService.getNoticeList(title, pageable);
        return ResponseEntity.ok(noticeList);
    }

    // Get a corresponding notice
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeDto.Response> getNotice(@PathVariable Long noticeId) {
        NoticeDto.Response notice = noticeService.getNotice(noticeId);
        return ResponseEntity.ok(notice);
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
