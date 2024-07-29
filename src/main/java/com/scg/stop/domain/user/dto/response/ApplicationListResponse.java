package com.scg.stop.domain.user.dto.response;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
public class ApplicationListResponse {

    private Long id;
    private String name;
    private String division;
    private String position;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
