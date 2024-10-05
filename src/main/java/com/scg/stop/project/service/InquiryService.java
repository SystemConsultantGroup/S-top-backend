package com.scg.stop.project.service;

import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.global.infrastructure.EmailService;
import com.scg.stop.project.domain.Inquiry;
import com.scg.stop.project.domain.InquiryReply;
import com.scg.stop.project.dto.request.InquiryReplyRequest;
import com.scg.stop.project.dto.request.InquiryRequest;
import com.scg.stop.project.dto.response.InquiryDetailResponse;
import com.scg.stop.project.dto.response.InquiryReplyResponse;
import com.scg.stop.project.dto.response.InquiryResponse;
import com.scg.stop.project.repository.InquiryReplyRepository;
import com.scg.stop.project.repository.InquiryRepository;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.domain.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryReplyRepository inquiryReplyRepository;
    private final EmailService emailService;

    // 문의 목록 조회
    @Transactional(readOnly = true)
    public Page<InquiryResponse> getInquiryList(String title, Pageable pageable) {
        Page<Inquiry> inquiries = inquiryRepository.findInquiries(title, pageable);
        return inquiries.map(inquiry ->
                InquiryResponse.of(
                        inquiry.getId(),
                        inquiry.getUser() == null ? "탈퇴한 회원" : inquiry.getUser().getName(),
                        inquiry.getTitle(),
                        inquiry.getCreatedAt()
                )
        );
    }

    // 문의 상세 조회
    @Transactional(readOnly = true)
    public InquiryDetailResponse getInquiry(Long inquiryId, User user) {
        Inquiry inquiry = findInquiryById(inquiryId);

        if (user.getUserType() != UserType.ADMIN && !inquiry.getUser().getId().equals(user.getId())) {
            throw new BadRequestException(ExceptionCode.UNAUTHORIZED_USER);
        }

        return InquiryDetailResponse.of(
                inquiry.getId(),
                inquiry.getUser() == null ? "탈퇴한 회원" : inquiry.getUser().getName(),
                inquiry.getProject().getId(),
                inquiry.getProject().getName(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getCreatedAt(),
                inquiry.getUpdatedAt()
        );
    }

    // 문의 수정
    // TODO: Email 전송 로직 추가
    public InquiryDetailResponse updateInquiry(Long inquiryId, User user, InquiryRequest inquiryUpdateRequest) {
        Inquiry inquiry = findInquiryById(inquiryId);

        if (user.getUserType() != UserType.ADMIN && !inquiry.getUser().getId().equals(user.getId())) {
            throw new BadRequestException(ExceptionCode.UNAUTHORIZED_USER);
        }

        inquiry.updateInquiry(inquiryUpdateRequest.getTitle(), inquiryUpdateRequest.getContent());

        return InquiryDetailResponse.of(
                inquiry.getId(),
                inquiry.getUser() == null ? "탈퇴한 회원" : inquiry.getUser().getName(),
                inquiry.getProject().getId(),
                inquiry.getProject().getName(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getCreatedAt(),
                inquiry.getUpdatedAt()
        );
    }

    // 문의 삭제
    public void deleteInquiry(Long inquiryId, User user) {
        Inquiry inquiry = findInquiryById(inquiryId);

        if (user.getUserType() != UserType.ADMIN && !inquiry.getUser().getId().equals(user.getId())) {
            throw new BadRequestException(ExceptionCode.UNAUTHORIZED_USER);
        }

        inquiryRepository.delete(inquiry);
    }

    // 문의 답변 등록
    public InquiryReplyResponse createInquiryReply(Long inquiryId, InquiryReplyRequest inquiryReplyCreateRequest) {
        Inquiry inquiry = findInquiryById(inquiryId);

        if (inquiry.getReply() != null) {
            throw new BadRequestException(ExceptionCode.ALREADY_EXIST_INQUIRY_REPLY);
        }

        InquiryReply inquiryReply = InquiryReply.createInquiryReply(
                inquiryReplyCreateRequest.getTitle(),
                inquiryReplyCreateRequest.getContent(),
                inquiry
        );
        inquiryReplyRepository.save(inquiryReply);

        if (inquiry.getUser() != null) {
            emailService.sendEmail(inquiry.getUser().getEmail(), inquiryReply.getTitle(), inquiryReply.getContent());
        }

        return InquiryReplyResponse.of(
                inquiryReply.getId(),
                inquiryReply.getTitle(),
                inquiryReply.getContent()
        );
    }

    // 문의 답변 조회
    @Transactional(readOnly = true)
    public InquiryReplyResponse getInquiryReply(Long inquiryId, User user) {
        Inquiry inquiry = findInquiryById(inquiryId);

        if (user.getUserType() != UserType.ADMIN && !inquiry.getUser().getId().equals(user.getId())) {
            throw new BadRequestException(ExceptionCode.UNAUTHORIZED_USER);
        }
        
        InquiryReply inquiryReply = inquiry.getReply();

        if (inquiryReply == null) {
            throw new BadRequestException(ExceptionCode.NOT_FOUND_INQUIRY_REPLY);
        }

        return InquiryReplyResponse.of(
                inquiryReply.getId(),
                inquiryReply.getTitle(),
                inquiryReply.getContent()
        );
    }

    // 문의 답변 수정
    // TODO: Email 전송 로직 추가
    public InquiryReplyResponse updateInquiryReply(Long inquiryId, InquiryReplyRequest inquiryReplyUpdateRequest) {
        Inquiry inquiry = findInquiryById(inquiryId);
        InquiryReply inquiryReply = inquiry.getReply();

        if (inquiryReply == null) {
            throw new BadRequestException(ExceptionCode.NOT_FOUND_INQUIRY_REPLY);
        }

        inquiryReply.updateInquiryResponse(
                inquiryReplyUpdateRequest.getTitle(),
                inquiryReplyUpdateRequest.getContent()
        );

        return InquiryReplyResponse.of(
                inquiryReply.getId(),
                inquiryReply.getTitle(),
                inquiryReply.getContent()
        );
    }

    // 문의 답변 삭제
    public void deleteInquiryReply(Long inquiryId) {
        Inquiry inquiry = findInquiryById(inquiryId);
        InquiryReply inquiryReply = inquiry.getReply();

        if (inquiryReply == null) {
            throw new BadRequestException(ExceptionCode.NOT_FOUND_INQUIRY_REPLY);
        }

        inquiry.deleteReply();
    }

    // helper method for finding inquiry by id
    private Inquiry findInquiryById(Long inquiryId) {
        return inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_INQUIRY));
    }
}
