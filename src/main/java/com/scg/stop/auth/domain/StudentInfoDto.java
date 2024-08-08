package com.scg.stop.auth.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentInfoDto {

    @NotBlank
    private String department;

    @NotBlank
    private String studentNumber;
}
