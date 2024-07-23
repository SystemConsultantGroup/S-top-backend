package com.scg.stop.domain.notice.dto.response;

import com.scg.stop.domain.notice.domain.Notice;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResponseDto {
    private Long id;
    private String title;
    private String content;
    private Integer hitCount;
    private boolean fixed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
//     private List<String> fileUuids; // attached files uuid


    // Entity -> DTO
    // TODO: handle attached files uuid
    public static NoticeResponseDto from(Notice notice) {
       return new NoticeResponseDto(
              notice.getId(),
              notice.getTitle(),
              notice.getContent(),
              notice.getHitCount(),
              notice.isFixed(),
              notice.getCreatedAt(),
              notice.getUpdatedAt()
         );
    }
}