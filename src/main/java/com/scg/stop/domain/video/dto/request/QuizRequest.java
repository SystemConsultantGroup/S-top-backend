package com.scg.stop.domain.video.dto.request;

import com.scg.stop.domain.video.domain.Quiz;
import com.scg.stop.domain.video.domain.QuizInfo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class QuizRequest {

    @NotNull(message = "퀴즈를 입력해주세요.")
    public Map<String, QuizInfo> quiz;

    public Quiz toEntity() { return Quiz.from(this); }
}
