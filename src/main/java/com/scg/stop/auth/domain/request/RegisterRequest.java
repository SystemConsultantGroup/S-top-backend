package com.scg.stop.auth.domain.request;

import com.scg.stop.auth.domain.StudentInfoDto;
import com.scg.stop.user.domain.Student;
import com.scg.stop.user.domain.UserType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phoneNumber;

    @NotNull(message = "회원 유형을 입력해주세요.")
    private UserType userType;

    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    private String signUpSource;

    private StudentInfoDto studentInfo;

}
