package com.scg.stop.user.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.scg.stop.user.domain.Application;
import com.scg.stop.user.domain.Student;
import com.scg.stop.user.domain.User;
import com.scg.stop.user.domain.UserType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String phone;
    private String email;
    private String socialLoginId;
    private UserType userType;
    private String division; // UserType.PROFESSOR, UserType.COMPANY
    private String position; // UserType.PROFESSOR, UserType.COMPANY
    private String studentNumber; // UserType.STUDENT
    private String departmentName; // UserType.STUDENT
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponse of(User user, String division, String position, String studentNumber, String departmentName) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getPhone(),
                user.getEmail(),
                user.getSocialLoginId(),
                user.getUserType(),
                division,
                position,
                studentNumber,
                departmentName,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
