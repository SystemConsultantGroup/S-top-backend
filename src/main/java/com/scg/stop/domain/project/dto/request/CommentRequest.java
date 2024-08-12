package com.scg.stop.domain.project.dto.request;

import com.scg.stop.domain.project.domain.Comment;
import com.scg.stop.domain.project.domain.Project;
import com.scg.stop.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequest {
    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    @NotNull(message = "익명 여부를 입력해주세요")
    private Boolean isAnonymous;

    public Comment toEntity(Project project, User user) {
        return new Comment(
                null,
                content,
                isAnonymous,
                project,
                user
        );
    }
}
