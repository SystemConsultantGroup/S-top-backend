package com.scg.stop.domain.video.dto.response;

import com.scg.stop.domain.video.domain.Talk;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TalkResponse {
    public Long id;
    public String title;
    public String youtubeId;
    public Integer year;
    public boolean hasQuiz;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;
    //TODO: QUIZ

    public static TalkResponse from(Talk talk) {
        return new TalkResponse(
                talk.getId(),
                talk.getTitle(),
                talk.getYoutubeId(),
                talk.getYear(),
                talk.isHasQuiz(),
                talk.getCreatedAt(),
                talk.getUpdatedAt()
        );
    }

}
