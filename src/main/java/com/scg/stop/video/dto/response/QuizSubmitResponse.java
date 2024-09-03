package com.scg.stop.video.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class QuizSubmitResponse {
    public boolean success;
    public Integer tryCount;
}
