package com.scg.stop.domain.gallery.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
public class UpdateGalleryRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "연도를 입력해주세요.")
    private int year;

    @NotNull(message = "월을 입력해주세요.")
    private int month;

    @Size(min = 1, message = "1개 이상의 파일을 첨부해야 합니다.")
    @NotNull(message = "파일을 첨부해주세요.")
    private List<Long> fileIds;
}