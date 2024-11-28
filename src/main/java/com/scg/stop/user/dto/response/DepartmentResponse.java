package com.scg.stop.user.dto.response;

import com.scg.stop.user.domain.Department;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DepartmentResponse {

    private Long id;
    private String name;

    public static DepartmentResponse from(Department department) {
        return new DepartmentResponse(
                department.getId(),
                department.getName()
        );
    }
}
