package com.scg.stop.proposal.domain.request;

import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Getter
public class ProposalRequest {

    @NotBlank(message = "과제 웹사이트를 입력해주세요.")
    private final String webSite;

    @NotBlank(message = "과제 이름을 입력해주세요.")
    private final String title;

    @NotBlank(message = "알림을 받을 이메일을 입력해주세요.")
    private final String email;

    @NotBlank(message = "과제 개요를 입력해주세요.")
    private final String description;

    @NotBlank(message = "과제 유형을 입력해주세요.")
    private final String projectTypes;

    @NotBlank(message = "과제 내용을 입력해주세요.")
    private final String content;

    @NotBlank(message = "과제의 공개여부를 입력해주세요.")
    private Boolean isVisible;

    @NotBlank(message = "과제의 익명여부를 입력해주세요.")
    private Boolean isAnonymous;
//    private String fileUuid;

    public ProposalRequest(
            String webSite,
            String title,
            String email,
            String description,
            String projectTypes,
            String content,
            String isVisible,
            String isAnonymous
    ) {
        validateProjectTypes(projectTypes);
        validateAndCastBoolean(isVisible, isAnonymous);
        this.webSite = webSite;
        this.email = email;
        this.title = title;
        this.description = description;
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

    //    public Proposal toEntity(String webSite, String title, String description, String content) {
//        return new Proposal()
//    }
    private void validateProjectTypes(String projectTypes) {
        String PROJECT_PATTERN = "^([\\w가-힣]+)(, [\\w가-힣]+)*$";

        if (!projectTypes.matches(PROJECT_PATTERN)) {
            throw new BadRequestException(ExceptionCode.INVALID_PROJECT_TYPE);
        }
    }
}
