package com.scg.stop.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InquiryReplyRequest {


    @NotBlank(message = "문의 답변 제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "문의 답변 내용을 입력해주세요.")
    private String content;
}
