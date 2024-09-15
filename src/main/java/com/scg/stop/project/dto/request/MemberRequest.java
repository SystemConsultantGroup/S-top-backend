package com.scg.stop.project.dto.request;

import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.project.domain.Member;
import com.scg.stop.project.domain.Project;
import com.scg.stop.project.domain.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class MemberRequest {

    @NotBlank(message = "멤버 이름을 입력해주세요")
    private String name;

    @NotNull(message = "멤버 역할을 입력해주세요")
    private Role role;

    public Member toEntity(Project project) {
        return new Member(
                null,
                name,
                role,
                project
        );
    }
}