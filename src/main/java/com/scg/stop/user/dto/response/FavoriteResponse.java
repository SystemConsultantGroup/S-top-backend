package com.scg.stop.user.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FavoriteResponse {
    private Long id;
    private String title;
    private String youtubeId;

    public static FavoriteResponse of(Long id, String title, String youtubeId) {
        return new FavoriteResponse(id, title, youtubeId);
    }
}
