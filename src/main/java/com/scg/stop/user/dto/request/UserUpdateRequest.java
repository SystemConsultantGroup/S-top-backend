package com.scg.stop.user.dto.request;

import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.ExceptionCode;
import com.scg.stop.user.domain.UserType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class UserUpdateRequest {

    private String name;
    private String phone;
    private String email;

    private UserType userType;

    // UserType.PROFESSOR, UserType.COMPANY
    private String division;
    private String position;

    // UserType.STUDENT
    private String studentNumber;
    private String departmentName;

    public UserUpdateRequest(String name, String phone, String email, UserType userType,
                             String division, String position, String studentNumber, String departmentName) {
        validateStudentInfo(userType, studentNumber, departmentName);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.userType = userType;
        this.division = division;
        this.position = position;
        this.studentNumber = studentNumber;
        this.departmentName = departmentName;
    }

    private void validateStudentInfo(UserType userType, String studentNumber, String departmentName) {
        if(userType.equals(UserType.STUDENT)){
            if(studentNumber == null || departmentName == null){
                throw new BadRequestException(ExceptionCode.INVALID_STUDENTINFO);
            }
        }
    }

}
