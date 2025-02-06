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
        List<String> keys = new ArrayList<>(quiz.getQuiz().keySet());
        Collections.sort(keys);
        List<QuizInfo> tmp = new ArrayList<>();
        for(String key : keys) {
            tmp.add(quiz.getQuiz().get(key));
        }
        return new QuizResponse(tmp);
    }
}
