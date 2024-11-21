package com.scg.stop.notice.controller;

import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.notice.dto.request.NoticeRequest;
import com.scg.stop.notice.dto.response.NoticeListElementResponse;
import com.scg.stop.notice.dto.response.NoticeResponse;
import com.scg.stop.notice.service.NoticeService;
import com.scg.stop.user.domain.AccessType;
import com.scg.stop.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public ResponseEntity<NoticeResponse> createNotice(
            @RequestBody @Valid NoticeRequest createNoticeDto,
            @AuthUser(accessType = {AccessType.ADMIN}) User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(noticeService.createNotice(createNoticeDto));
    }

    // Get a list of notices
    @GetMapping
    public ResponseEntity<Page<NoticeListElementResponse>> getNoticeList(
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "searchScope", defaultValue = "both") String searchScope,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        // Enforce that searchScope is ignored if searchTerm is null
        if (searchTerm == null || searchTerm.isEmpty()) {
            searchScope = null; // Ignore search scope
        }

        Page<NoticeListElementResponse> noticeList = noticeService.getNoticeList(searchTerm, searchScope, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(noticeList);
    }


    // Get a corresponding notice
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponse> getNotice(
            @PathVariable("noticeId") Long noticeId) {
        NoticeResponse notice = noticeService.getNotice(noticeId);
        return ResponseEntity.status(HttpStatus.OK).body(notice);
    }

    // Update a corresponding notice
    @PutMapping("/{noticeId}")
    public ResponseEntity<NoticeResponse> updateNotice(
            @PathVariable("noticeId") Long noticeId,
            @RequestBody @Valid NoticeRequest updateNoticeDto,
            @AuthUser(accessType = {AccessType.ADMIN}) User user) {
        return ResponseEntity.status(HttpStatus.OK).body(noticeService.updateNotice(noticeId, updateNoticeDto));
    }

    // Delete a corresponding notice
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Long> deleteNotice(
            @PathVariable("noticeId") Long noticeId,
            @AuthUser(accessType = {AccessType.ADMIN}) User user) {
        noticeService.deleteNotice(noticeId);
        return ResponseEntity.noContent().build();
    }
}