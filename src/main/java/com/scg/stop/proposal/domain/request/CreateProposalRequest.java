package com.scg.stop.proposal.domain.request;

import com.scg.stop.domain.project.domain.ProjectType;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Getter;


@Getter
public class CreateProposalRequest {

    private final String webSite;

    @NotBlank(message = "과제 이름을 입력해주세요.")
    private final String title;

    @NotBlank(message = "알림을 받을 이메일을 입력해주세요.")
    private final String email;

    @NotEmpty(message = "과제 유형을 입력해주세요.")
    private final List<ProjectType> projectTypes;

    @NotBlank(message = "과제 내용을 입력해주세요.")
    private final String content;

    @NotNull(message = "과제의 공개여부를 입력해주세요.")
    private Boolean isVisible;

    @NotNull(message = "과제의 익명여부를 입력해주세요.")
    private Boolean isAnonymous;
//    private String fileUuid;

    public CreateProposalRequest(
            String webSite,
            String title,
            String email,
            List<ProjectType> projectTypes,
            String content,
            String isVisible,
            String isAnonymous
    ) {
        validateAndCastBoolean(isVisible, isAnonymous);
        this.webSite = webSite;
        this.email = email;
        this.title = title;
        this.content = content;
        this.projectTypes = projectTypes;
    }

    private void validateAndCastBoolean(String isVisible, String isAnonymous) {
        if (isVisible.equals("true")) this.isVisible = true;
        else if (isVisible.equals("false")) this.isVisible = false;
        else throw new BadRequestException(ExceptionCode.INVALID_REQUEST);

        if (isAnonymous.equals("true")) this.isAnonymous = true;
        else if (isAnonymous.equals("false")) this.isAnonymous = false;
        else throw new BadRequestException(ExceptionCode.INVALID_REQUEST);

    }

}
