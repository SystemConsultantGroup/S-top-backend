package com.scg.stop.event.dto.response;

import com.scg.stop.file.domain.File;
import com.scg.stop.file.dto.response.FileResponse;
import com.scg.stop.event.domain.EventNotice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventNoticeResponse {
    private Long id;
    private String title;
    private String content;
    private Integer hitCount;
    private boolean fixed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<FileResponse> files;

    // create a new EventNoticeResponse object
    public static EventNoticeResponse from(EventNotice eventNotice, List<File> files) {

        List<FileResponse> fileResponses = files.stream()
                .map(FileResponse::from)
                .collect(java.util.stream.Collectors.toList());

        return new EventNoticeResponse(
                eventNotice.getId(),
                eventNotice.getTitle(),
                eventNotice.getContent(),
                eventNotice.getHitCount(),
                eventNotice.isFixed(),
                eventNotice.getCreatedAt(),
                eventNotice.getUpdatedAt(),
                fileResponses
        );
    }
}
