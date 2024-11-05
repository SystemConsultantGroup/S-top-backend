package com.scg.stop.video.dto.request;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.scg.stop.video.domain.Talk;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TalkRequest {

    @NotBlank(message="제목을 입력해주세요.")
    public String title;

    @NotBlank(message="유튜브 URL의 ID를 입력해주세요.")
    public String youtubeId;
    
    @NotNull(message = "연도를 입력해주세요.")
    public Integer year;

    @NotBlank(message = "대담자의 소속을 입력해주세요.")
    public String talkerBelonging;

    @NotBlank(message = "대담자의 성명을 입력해주세요.")
    public String talkerName;

    @JsonUnwrapped
    @Valid
    public QuizRequest quiz;

    public Talk toEntity() {return Talk.from(
            title, youtubeId, year, talkerBelonging, talkerName
    ); }
}
