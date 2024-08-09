package com.scg.stop.video.dto.request;

import com.scg.stop.video.domain.QuizInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class QuizInfoRequest {
    @NotBlank(message = "퀴즈의 질문을 입력해주세요.")
    private String question;

    @NotNull(message = "올바른 퀴즈의 정답번호를 입력해주세요.")
    private Integer answer;

    @NotEmpty(message = "1개 이상의 퀴즈 선지를 입력해주세요.")
    private List<String> options;

    public QuizInfo toQuizInfo() {
        QuizInfo quizInfo = new QuizInfo();
        quizInfo.setQuestion(question);
        quizInfo.setAnswer(answer);
        quizInfo.setOptions(options);
        return quizInfo;
    }

}
