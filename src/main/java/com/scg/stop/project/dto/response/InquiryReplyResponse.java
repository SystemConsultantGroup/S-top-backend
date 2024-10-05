package com.scg.stop.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InquiryReplyResponse {

    private Long id;
    private String title;
    private String content;

    public static InquiryReplyResponse of(Long id, String title, String content) {
        return new InquiryReplyResponse(id, title, content);
    }
    
}
