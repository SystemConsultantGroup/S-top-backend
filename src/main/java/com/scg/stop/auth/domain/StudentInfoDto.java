package com.scg.stop.auth.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class StudentInfoDto {

        @NotBlank
        private String department;

        @NotBlank
        private String studentNumber;
}
