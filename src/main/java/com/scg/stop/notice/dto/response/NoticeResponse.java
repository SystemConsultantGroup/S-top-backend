package com.scg.stop.notice.dto.response;

import com.scg.stop.file.domain.File;
import com.scg.stop.file.dto.response.FileResponse;
import com.scg.stop.notice.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResponse {
    private Long id;
    private String title;
    private String content;
    private Integer hitCount;
    private boolean fixed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<FileResponse> files;

    // create a new NoticeResponse object
    public static NoticeResponse from(Notice notice, List<File> files) {

        List<FileResponse> fileResponses = files.stream()
                .map(FileResponse::from)
                .collect(Collectors.toList());

        return new NoticeResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getHitCount(),
                notice.isFixed(),
                notice.getCreatedAt(),
                notice.getUpdatedAt(),
                fileResponses
        );
    }
}