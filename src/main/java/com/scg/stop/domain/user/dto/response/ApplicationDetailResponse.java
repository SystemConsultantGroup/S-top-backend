package com.scg.stop.domain.user.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.scg.stop.domain.user.domain.UserType;
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
}
