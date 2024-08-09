package com.scg.stop.domain.file.dto.response;

import static lombok.AccessLevel.PRIVATE;

import com.scg.stop.domain.file.domain.File;
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

    public static FileResponse from(File file) {
        return new FileResponse(
                file.getId(),
                file.getUuid(),
                file.getName(),
                file.getMimeType(),
                file.getCreatedAt(),
                file.getUpdatedAt()
        );
    }
}