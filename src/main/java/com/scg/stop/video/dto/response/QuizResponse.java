package com.scg.stop.video.dto.response;

import com.scg.stop.video.domain.Quiz;
import com.scg.stop.video.domain.QuizInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QuizResponse {
    public List<QuizInfo> quiz;

    public static QuizResponse from(Quiz quiz) {
        return from(quiz, true);
    }

    public static QuizResponse from(Quiz quiz, boolean revealAnswer) {
        List<String> keys = new ArrayList<>(quiz.getQuiz().keySet());
        Collections.sort(keys);
        List<QuizInfo> tmp = new ArrayList<>();
        for(String key : keys) {
            QuizInfo quizInfo = quiz.getQuiz().get(key);
            tmp.add(new QuizInfo(
                    quizInfo.getQuestion(),
                    revealAnswer ? quizInfo.getAnswer() : -1,
                    quizInfo.getOptions()
            ));
        }
        return new QuizResponse(tmp);
    }
}
