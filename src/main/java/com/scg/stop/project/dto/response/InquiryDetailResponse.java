package com.scg.stop.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class InquiryDetailResponse {

    private Long id;
    private String authorName; // 문의 작성자 이름
    private Long projectId; // 문의 대상 프로젝트 ID
    private String projectName; // 문의 대상 프로젝트 이름
    private String title;
    private String content;
    private Boolean replied; // 답변 등록 여부
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static InquiryDetailResponse of(Long id, String authorName, Long projectId, String projectName, String title, String content, Boolean replied, LocalDateTime createdAt, LocalDateTime updatedAt) {
        return new InquiryDetailResponse(id, authorName, projectId, projectName, title, content, replied, createdAt, updatedAt);
    }
}
