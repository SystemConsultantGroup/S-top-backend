package com.scg.stop.video.dto.request;

import com.scg.stop.video.domain.Quiz;
import com.scg.stop.video.domain.QuizInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class QuizRequest {

    //@NotNull(message = "퀴즈를 입력해주세요.")
    @Size(min=1, message = "퀴즈는 1개 이상이어야 합니다.")
    @Valid
    public List<@Valid QuizInfoRequest> quiz;

    public Quiz toEntity() { return Quiz.from(
            this.toQuizInfoMap()
    ); }

    public Map<String, QuizInfo> toQuizInfoMap() {
        Map<String, QuizInfo> quizInfoMap = new HashMap<>();
        for(int i = 0; i < quiz.size(); i++) {
            quizInfoMap.put(Integer.toString(i), quiz.get(i).toQuizInfo());
        }
        return quizInfoMap;
    }

}
