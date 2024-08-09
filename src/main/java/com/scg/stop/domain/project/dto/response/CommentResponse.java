package com.scg.stop.domain.project.dto.response;

import com.scg.stop.domain.project.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private Long id;
    private Long projectId;
    private String userName;
    private Boolean isAnonymous;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CommentResponse of(Comment comment){
        return new CommentResponse(
                comment.getId(),
                comment.getProject().getId(),
                comment.getIsAnonymous() ? null : comment.getUser().getName(), // 익명이 아닐 경우에만 사용자 이름을 반환
                comment.getIsAnonymous(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
