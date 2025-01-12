package com.scg.stop.project.controller;

import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.project.dto.request.InquiryReplyRequest;
import com.scg.stop.project.dto.request.InquiryRequest;
import com.scg.stop.project.dto.response.InquiryDetailResponse;
import com.scg.stop.project.dto.response.InquiryReplyResponse;
import com.scg.stop.project.dto.response.InquiryResponse;
import com.scg.stop.project.service.InquiryService;
import com.scg.stop.user.domain.AccessType;
import com.scg.stop.user.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;

    // 문의 목록 조회
    @GetMapping()
    public ResponseEntity<Page<InquiryResponse>> getInquiries(
            @AuthUser(accessType = {AccessType.COMPANY, AccessType.ADMIN}) User user,
            @RequestParam(value = "terms", required = false) String searchTerm,
            @RequestParam(value = "scope", defaultValue = "both") String searchScope,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

        // Enforce that searchScope is ignored if searchTerm is null
        if (searchTerm == null || searchTerm.isEmpty()) {
            searchScope = null; // Ignore search scope
        }

        Page<InquiryResponse> inquiryList = inquiryService.getInquiryList(searchTerm, searchScope, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(inquiryList);
    }

    // 문의 상세 조회
    @GetMapping("/{inquiryId}")
    public ResponseEntity<InquiryDetailResponse> getInquiry(
            @AuthUser(accessType = {AccessType.COMPANY, AccessType.ADMIN}) User user,
            @PathVariable("inquiryId") Long inquiryId) {

        InquiryDetailResponse inquiryDetailResponse = inquiryService.getInquiry(inquiryId, user);
        return ResponseEntity.status(HttpStatus.OK).body(inquiryDetailResponse);

    }

    // 문의 수정
    @PutMapping("/{inquiryId}")
    public ResponseEntity<InquiryDetailResponse> updateInquiry(
            @AuthUser(accessType = {AccessType.COMPANY, AccessType.ADMIN}) User user,
            @PathVariable("inquiryId") Long inquiryId,
            @RequestBody @Valid InquiryRequest inquiryUpdateRequest) {

        InquiryDetailResponse inquiryDetailResponse = inquiryService.updateInquiry(inquiryId, user,
                inquiryUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(inquiryDetailResponse);
    }

    // 문의 삭제
    @DeleteMapping("/{inquiryId}")
    public ResponseEntity<Void> deleteInquiry(
            @AuthUser(accessType = {AccessType.ADMIN, AccessType.COMPANY}) User user,
            @PathVariable("inquiryId") Long inquiryId) {

        inquiryService.deleteInquiry(inquiryId, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // 문의 답변 등록
    @PostMapping("/{inquiryId}/reply")
    public ResponseEntity<InquiryReplyResponse> createInquiryReply(
            @AuthUser(accessType = {AccessType.ADMIN}) User user,
            @PathVariable("inquiryId") Long inquiryId,
            @RequestBody @Valid InquiryReplyRequest inquiryReplyRequest) {

        InquiryReplyResponse inquiryReplyResponse = inquiryService.createInquiryReply(inquiryId, inquiryReplyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(inquiryReplyResponse);
    }

    // 문의 답변 조회
    @GetMapping("/{inquiryId}/reply")
    public ResponseEntity<InquiryReplyResponse> getInquiryReply(
            @AuthUser(accessType = {AccessType.COMPANY, AccessType.ADMIN}) User user,
            @PathVariable("inquiryId") Long inquiryId) {

        InquiryReplyResponse inquiryReplyResponse = inquiryService.getInquiryReply(inquiryId, user);
        return ResponseEntity.status(HttpStatus.OK).body(inquiryReplyResponse);
    }

    // 문의 답변 수정
    @PutMapping("/{inquiryId}/reply")
    public ResponseEntity<InquiryReplyResponse> updateInquiryReply(
            @AuthUser(accessType = {AccessType.ADMIN}) User user,
            @PathVariable("inquiryId") Long inquiryId,
            @RequestBody @Valid InquiryReplyRequest inquiryReplyRequest) {

        InquiryReplyResponse inquiryReplyResponse = inquiryService.updateInquiryReply(inquiryId, inquiryReplyRequest);
        return ResponseEntity.status(HttpStatus.OK).body(inquiryReplyResponse);
    }

    // 문의 답변 삭제
    @DeleteMapping("/{inquiryId}/reply")
    public ResponseEntity<Void> deleteInquiryReply(
            @AuthUser(accessType = {AccessType.ADMIN}) User user,
            @PathVariable("inquiryId") Long inquiryId) {

        inquiryService.deleteInquiryReply(inquiryId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
