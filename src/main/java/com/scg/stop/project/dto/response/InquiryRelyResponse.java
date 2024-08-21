package com.scg.stop.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InquiryRelyResponse {

    private Long id;
    private String title;
    private String content;

    public static InquiryRelyResponse of(Long id, String title, String content) {
        return new InquiryRelyResponse(id, title, content);
    }
}
