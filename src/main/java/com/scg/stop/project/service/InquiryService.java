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

    @Transactional(readOnly = true)
    public Page<InquiryResponse> getInquiryList(String title, Pageable pageable) {
        Page<Inquiry> inquiries = inquiryRepository.findInquiries(title, pageable);
        return inquiries.map(inquiry -> InquiryResponse.of(
                inquiry.getId(),
                inquiry.getUser().getName(),
                inquiry.getTitle(),
                inquiry.getCreatedAt()
        ));
    }


    @Transactional(readOnly = true)
    public InquiryDetailResponse getInquiry(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_INQUIRY));
        return InquiryDetailResponse.of(
                inquiry.getId(),
                inquiry.getUser().getName(),
                inquiry.getProject().getId(),
                inquiry.getProject().getName(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getCreatedAt(),
                inquiry.getUpdatedAt()
        );
    }

    public InquiryDetailResponse updateInquiry(Long inquiryId, InquiryRequest inquiryUpdateRequest) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_INQUIRY));
        inquiry.updateInquiry(inquiryUpdateRequest.getTitle(), inquiryUpdateRequest.getContent());
        return InquiryDetailResponse.of(
                inquiry.getId(),
                inquiry.getUser().getName(),
                inquiry.getProject().getId(),
                inquiry.getProject().getName(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getCreatedAt(),
                inquiry.getUpdatedAt()
        );
    }

    public void deleteInquiry(Long inquiryId) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_INQUIRY));
        inquiryRepository.delete(inquiry);
    }

    public InquiryReplyResponse createInquiryReply(Long inquiryId, InquiryReplyRequest inquiryReplyCreateRequest) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_INQUIRY));

        if (inquiry.getReply() != null) {
            throw new BadRequestException(ExceptionCode.ALREADY_EXIST_INQUIRY_REPLY);
        }

        InquiryReply inquiryReply = InquiryReply.createInquiryReply(inquiryReplyCreateRequest.getTitle(),
                inquiryReplyCreateRequest.getContent(),
                inquiry);
        inquiryReplyRepository.save(inquiryReply);
        emailService.sendEmail(inquiry.getUser().getEmail(), inquiryReply.getTitle(), inquiryReply.getContent());
        return InquiryReplyResponse.of(inquiryReply.getId(), inquiryReply.getTitle(), inquiryReply.getContent());
    }

    public InquiryReplyResponse updateInquiryReply(Long inquiryId, InquiryReplyRequest inquiryReplyUpdateRequest) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_INQUIRY));

        InquiryReply inquiryReply = inquiry.getReply();
        if (inquiryReply == null) {
            throw new BadRequestException(ExceptionCode.NOT_FOUND_INQUIRY_REPLY);
        }
        inquiryReply.updateInquiryResponse(inquiryReplyUpdateRequest.getTitle(), inquiryReplyUpdateRequest.getContent());
        inquiry.updateReply(inquiryReply);
        return InquiryReplyResponse.of(inquiryReply.getId(), inquiryReply.getTitle(), inquiryReply.getContent());
    }

    public void deleteInquiryReply(Long inquiryId) {

        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_INQUIRY));
        InquiryReply inquiryReply = inquiry.getReply();
        inquiryReplyRepository.delete(inquiryReply);
        inquiry.updateReply(null);
    }
}