package com.scg.stop.domain.project.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProjectResponse {
    private Long id;
    private String name;

    public static ProjectResponse of(Long id, String name) {
        return new ProjectResponse(id, name);
    }
}