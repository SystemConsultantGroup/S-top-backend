package com.scg.stop.domain.video.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class VideoResponse {
    private Long id;
    private String title;
    private String youtubeId;

    public static VideoResponse of(Long id, String title, String youtubeId) {
        return new VideoResponse(id, title, youtubeId);
    }
}
