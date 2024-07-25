package com.scg.stop.domain.gallery.dto.request;

import static lombok.AccessLevel.PRIVATE;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class CreateGalleryRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "연도를 입력해주세요.")
    private int year;

    @NotNull(message = "월을 입력해주세요.")
    private int month;

    private List<Long> fileIds;
}
