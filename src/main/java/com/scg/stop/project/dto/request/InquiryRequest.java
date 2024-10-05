package com.scg.stop.project.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InquiryRequest {

    @NotBlank(message = "문의 제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "문의 내용을 입력해주세요.")
    private String content;

}
