package com.scg.stop.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
}
