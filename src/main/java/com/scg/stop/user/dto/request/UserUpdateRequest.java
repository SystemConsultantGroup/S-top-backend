package com.scg.stop.user.dto.request;

import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static io.micrometer.common.util.StringUtils.isBlank;
import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
public class UserUpdateRequest {

    private String name;
    private String phone;
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
