package com.scg.stop.project.service;

import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.global.infrastructure.EmailService;
import com.scg.stop.project.domain.Inquiry;
import com.scg.stop.project.domain.Project;
import com.scg.stop.project.dto.request.InquiryRequest;
import com.scg.stop.project.dto.response.InquiryDetailResponse;
import com.scg.stop.project.repository.InquiryRepository;
import com.scg.stop.project.repository.ProjectRepository;
import com.scg.stop.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final InquiryRepository inquiryRepository;
    private final EmailService emailService;

    @Value("${spring.mail.adminEmail}")
    private String adminEmail;

    public InquiryDetailResponse createProjectInquiry(Long projectId, User user, InquiryRequest inquiryRequest) {

        // find the project by id
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_PROJECT));


        Inquiry inquiry = Inquiry.createInquiry(
                inquiryRequest.getTitle(),
                inquiryRequest.getContent(),
                project,
                user
        );

        inquiryRepository.save(inquiry);

        emailService.sendEmail(adminEmail, inquiry.getTitle(), inquiry.getContent());
        return InquiryDetailResponse.of(
                inquiry.getId(),
                user.getName(),
                project.getId(),
                project.getName(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getCreatedAt(),
                inquiry.getUpdatedAt()
        );

    }


}