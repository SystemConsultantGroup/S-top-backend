package com.scg.stop.project.service;

import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.global.infrastructure.EmailService;
import com.scg.stop.project.domain.Inquiry;
import com.scg.stop.project.dto.request.InquiryRequest;
import com.scg.stop.project.dto.response.InquiryDetailResponse;
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
}
