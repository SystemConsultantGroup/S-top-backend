package com.scg.stop.user.dto.response;

import com.scg.stop.domain.project.domain.Inquiry;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserInquiryResponse {

    private Long id;
    private String title;
    private Long projectId;
    private LocalDateTime createdDate;
    private boolean hasReply;

    public static UserInquiryResponse from(Inquiry inquiry) {
        return new UserInquiryResponse(
                inquiry.getId(),
                inquiry.getTitle(),
                inquiry.getProject() != null ? inquiry.getProject().getId() : null,
                inquiry.getCreatedAt(),
                inquiry.getReply() != null
        );
    }
}

