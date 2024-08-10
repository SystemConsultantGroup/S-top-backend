package com.scg.stop.video.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class QuizSubmitRequest {

    @NotNull(message = "퀴즈 제출 결과를 입력해주세요.")
    @Size(min = 1, message = "퀴즈 제출 결과를 1개 이상 입력해주세요.")
    Map<String, Integer> result;
}
