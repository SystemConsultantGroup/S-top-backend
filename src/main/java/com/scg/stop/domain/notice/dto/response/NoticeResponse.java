package com.scg.stop.domain.notice.dto.response;

import com.scg.stop.domain.file.dto.response.FileResponse;
import com.scg.stop.domain.notice.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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

    // Entity -> DTO
    public static NoticeResponse from(Notice notice, List<FileResponse> files) {
       return new NoticeResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getContent(),
                notice.getHitCount(),
                notice.isFixed(),
                notice.getCreatedAt(),
                notice.getUpdatedAt(),
                files
         );
    }
}