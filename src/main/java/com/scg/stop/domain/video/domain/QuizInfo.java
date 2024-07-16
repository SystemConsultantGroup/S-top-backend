package com.scg.stop.domain.video.domain;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuizInfo {

    private String question;
    private Integer answer;
    private List<String> options;
}
