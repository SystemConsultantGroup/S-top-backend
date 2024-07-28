package com.scg.stop.domain.gallery.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.scg.stop.domain.file.dto.response.FileResponse;
import com.scg.stop.domain.gallery.domain.Gallery;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
public class GalleryResponse {

    private Long id;
    private String title;
    private String content;
    private int year;
    private int month;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<FileResponse> files;

    public static GalleryResponse of(Gallery gallery, List<FileResponse> fileResponses) {
        return new GalleryResponse(
                gallery.getId(),
                gallery.getTitle(),
                gallery.getContent(),
                gallery.getYear(),
                gallery.getMonth(),
                gallery.getCreatedAt(),
                gallery.getUpdatedAt(),
                fileResponses
        );
    }
}
