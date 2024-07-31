package com.scg.stop.domain.video.dto.request;

import com.scg.stop.domain.video.domain.Talk;
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

    @NotNull(message = "퀴즈 여/부를 입력해주세요.")
    public boolean hasQuiz;
    
    @NotNull(message = "연도를 입력해주세요.")
    public Integer year;

    public Talk toEntity() {return Talk.from(this); }
}
