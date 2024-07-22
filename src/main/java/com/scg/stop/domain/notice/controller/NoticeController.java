package com.scg.stop.domain.notice.controller;

import com.scg.stop.domain.notice.dto.request.NoticeRequestDto;
import com.scg.stop.domain.notice.dto.response.NoticeResponseDto;
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
    public ResponseEntity<NoticeResponseDto> createNotice(@RequestBody @Valid NoticeRequestDto createNoticeDto) {
        return new ResponseEntity<>(noticeService.createNotice(createNoticeDto), HttpStatus.CREATED);
    }

    // Get a list of notices
    @GetMapping
    public ResponseEntity<Page<NoticeResponseDto>> getNoticeList(
            @RequestParam(value = "title", required = false) String title,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<NoticeResponseDto> noticeList = noticeService.getNoticeList(title, pageable);
        return ResponseEntity.ok(noticeList);
    }

    // Get a corresponding notice
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDto> getNotice(@PathVariable Long noticeId) {
        NoticeResponseDto notice = noticeService.getNotice(noticeId);
        return ResponseEntity.ok(notice);
    }

    // Update a corresponding notice
    @PutMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDto> updateNotice(@PathVariable Long noticeId, @RequestBody @Valid NoticeRequestDto updateNoticeDto) {
        return ResponseEntity.ok(noticeService.updateNotice(noticeId, updateNoticeDto));
    }

    // Delete a corresponding notice
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Long> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.noContent().build();
    }
}