package com.scg.stop.auth.domain.request;

import com.scg.stop.auth.domain.StudentInfoDto;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.user.domain.Student;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.domain.UserType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class RegisterRequest {

    @NotBlank(message = "이름을 입력해주세요.")
    private final String name;

    @NotBlank(message = "전화번호를 입력해주세요.")
    private final String phoneNumber;

    @NotNull(message = "회원 유형을 입력해주세요.")
    private final UserType userType;

    @NotBlank(message = "이메일을 입력해주세요.")
    private final String email;

    private final String signUpSource;

    private final StudentInfoDto studentInfo;

    public RegisterRequest(String name, String phoneNumber, UserType userType, String email, String signUpSource,
                           StudentInfoDto studentInfo) {
        validateStudentInfo(userType, studentInfo);
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.email = email;
        this.signUpSource = signUpSource;
        this.studentInfo = studentInfo;
    }

    private void validateStudentInfo(UserType userType, StudentInfoDto studentInfo) {
        if(userType.equals(UserType.STUDENT)){
            if(studentInfo.getStudentNumber() == null || studentInfo.getDepartment() == null){
                throw new BadRequestException(ExceptionCode.INVALID_STUDENTINFO);
            }
        }
    }
}
