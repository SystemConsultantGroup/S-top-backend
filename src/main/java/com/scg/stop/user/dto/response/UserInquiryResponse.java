package com.scg.stop.user.dto.response;

import com.scg.stop.domain.project.domain.Inquiry;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class UserInquiryResponse {

    private Long id;
    private String title;
    private Long projectId;
    private LocalDateTime createdDate;

    public static UserInquiryResponse from(Inquiry inquiry) {
        return new UserInquiryResponse(
                inquiry.getId(),
                inquiry.getTitle(),
                inquiry.getProject() != null ? inquiry.getProject().getId() : null,
                inquiry.getCreatedAt()
        );
    }
}

