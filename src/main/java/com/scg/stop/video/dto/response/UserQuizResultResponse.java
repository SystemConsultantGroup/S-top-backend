package com.scg.stop.video.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserQuizResultResponse {
    private Long user_id;
    private String name;
    private String phone;
    private String email;
    private Long successCount;
}
