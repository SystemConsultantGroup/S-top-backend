package com.scg.stop.user.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.scg.stop.user.domain.Application;
import com.scg.stop.user.domain.UserType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
public class ApplicationDetailResponse {

    private Long id;
    private String name;
    private String phone;
    private String email;
    private String division;
    private String position;
    private UserType userType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ApplicationDetailResponse from(Application application) {
        return new ApplicationDetailResponse(
                application.getId(),
                application.getUser().getName(),
                application.getUser().getPhone(),
                application.getUser().getEmail(),
                application.getDivision(),
                application.getPosition(),
                application.getUser().getUserType(),
                application.getCreatedAt(),
                application.getUpdatedAt()
        );
    }
}
