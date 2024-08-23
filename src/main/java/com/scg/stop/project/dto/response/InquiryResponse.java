package com.scg.stop.project.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class InquiryResponse {

    private Long id;
    private String name;
    private String title;
    private LocalDateTime createdAt;

    public static InquiryResponse of(Long id, String name, String title, LocalDateTime createdAt) {
        return new InquiryResponse(id, name, title, createdAt);
    }

}
