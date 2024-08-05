package com.scg.stop.domain.video.dto.response;

import com.scg.stop.domain.video.domain.Talk;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class TalksResponse {
    public Long id;
    public String title;
    public String youtubeId;
    public Integer year;
    public String talkerBelonging;
    public String talkerName;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public static TalksResponse from(Talk talk) {
        return new TalksResponse(
                talk.getId(),
                talk.getTitle(),
                talk.getYoutubeId(),
                talk.getYear(),
                talk.getTalkerBelonging(),
                talk.getTalkerName(),
                talk.getCreatedAt(),
                talk.getUpdatedAt()
        );
    }
}
