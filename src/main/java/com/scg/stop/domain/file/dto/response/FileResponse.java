package com.scg.stop.domain.file.dto.response;

import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
public class FileResponse {

    private Long id;
    private String uuid;
    private String name;
    private String mimeType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
