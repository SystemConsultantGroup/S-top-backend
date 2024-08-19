package com.scg.stop.domain.project.dto.response;

import com.scg.stop.domain.file.domain.File;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileResponse {
    private Long id;
    private String uuid;
    private String name;
    private String mimeType;

    public static FileResponse from(File file) {
        return new FileResponse(
                file.getId(),
                file.getUuid(),
                file.getName(),
                file.getMimeType()
        );
    }
}
