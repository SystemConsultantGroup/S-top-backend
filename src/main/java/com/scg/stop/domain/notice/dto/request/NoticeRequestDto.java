package com.scg.stop.domain.notice.dto.request;

import com.scg.stop.domain.notice.domain.Notice;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NoticeRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "고정 여부를 입력해주세요.")
    private boolean fixed;

    // DTO -> Entity
    public Notice toEntity() {
        return Notice.from(this);
    }
}