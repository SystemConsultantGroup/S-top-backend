package com.scg.stop.user.dto.request;

import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static io.micrometer.common.util.StringUtils.isBlank;
import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
public class UserUpdateRequest {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String phone;

    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    // UserType.PROFESSOR, UserType.COMPANY
    private String division;
    private String position;

    // UserType.STUDENT
    private String studentNumber;
    private String departmentName;

    public void validateStudentInfo() {
        if (isBlank(this.studentNumber) || isBlank(this.departmentName)) {
            throw new BadRequestException(ExceptionCode.INVALID_STUDENTINFO);
        }
    }

}
