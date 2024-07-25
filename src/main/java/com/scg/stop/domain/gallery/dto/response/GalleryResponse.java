package com.scg.stop.domain.gallery.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.scg.stop.domain.file.dto.response.FileResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class GalleryResponse {

    private Long id;
    private String title;
    private String content;
    private int year;
    private int month;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<FileResponse> files;
}
