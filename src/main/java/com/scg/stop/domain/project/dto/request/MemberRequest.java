package com.scg.stop.domain.project.dto.request;

import static lombok.AccessLevel.PROTECTED;

import com.scg.stop.domain.project.domain.Member;
import com.scg.stop.domain.project.domain.Project;
import com.scg.stop.domain.project.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class MemberRequest {

    private String name;
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