package com.scg.stop.video.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.scg.stop.video.domain.Talk;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TalkUserResponse {
    public Long id;
    public String title;
    public String youtubeId;
    public Integer year;
    public String talkerBelonging;
    public String talkerName;
    public boolean favorite;

    @JsonUnwrapped
    public QuizResponse quiz;

    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public static TalkUserResponse from(Talk talk) {
        return new TalkUserResponse(
                talk.getId(),
                talk.getTitle(),
                talk.getYoutubeId(),
                talk.getYear(),
                talk.getTalkerBelonging(),
                talk.getTalkerName(),
                false,
                (talk.getQuiz() != null)? QuizResponse.from(talk.getQuiz()) : new QuizResponse(),
                talk.getCreatedAt(),
                talk.getUpdatedAt()
        );
    }

    public static TalkUserResponse from(Talk talk, boolean favorite) {
        return new TalkUserResponse(
                talk.getId(),
                talk.getTitle(),
                talk.getYoutubeId(),
                talk.getYear(),
                talk.getTalkerBelonging(),
                talk.getTalkerName(),
                favorite,
                (talk.getQuiz() != null)? QuizResponse.from(talk.getQuiz()) : new QuizResponse(),
                talk.getCreatedAt(),
                talk.getUpdatedAt()
        );
    }
}
