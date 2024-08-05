package com.scg.stop.domain.video.dto.request;

import com.scg.stop.domain.video.domain.Quiz;
import com.scg.stop.domain.video.domain.QuizInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class QuizRequest {

    //@NotNull(message = "퀴즈를 입력해주세요.")
    @Size(min=1, message = "퀴즈는 1개 이상이어야 합니다.")
    @Valid
    public Map<String,
            @Valid QuizInfoRequest> quiz;

    public Quiz toEntity() { return Quiz.from(this); }

    public Map<String, QuizInfo> toQuizInfoMap() {
        return quiz.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toQuizInfo()));
    }

}
