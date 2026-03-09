package com.scg.stop.file.controller;

import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.file.domain.File;
import com.scg.stop.file.dto.response.FileResponse;
import com.scg.stop.file.service.FileService;
import com.scg.stop.user.domain.AccessType;
import com.scg.stop.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<List<FileResponse>> uploadFiles(@RequestPart("files") List<MultipartFile> files) {
        List<FileResponse> responses = fileService.uploadFiles(files);
        return ResponseEntity.status(HttpStatus.CREATED).body(responses);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<InputStreamResource> getFile(
            @PathVariable("fileId") Long fileId,
            WebRequest request) {
        File file = fileService.getFileMetadata(fileId);
        String etag = file.getUuid();

        if (request.checkNotModified(etag)) {
            return null;
        }

        InputStream stream = fileService.getFile(fileId);
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic())
                .eTag(etag)
                .contentType(MediaType.parseMediaType(file.getMimeType()))
                .body(new InputStreamResource(stream));
    }

    @GetMapping("/form/projects")
    public ResponseEntity<Resource> getProjectExcelForm(@AuthUser(accessType = AccessType.ADMIN) User user) {
        String directoryPath = "form/";
        String fileName = "project_upload_form.xlsx";
        Resource resource = fileService.getLocalFile(directoryPath, fileName);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", UriUtils.encode(fileName, StandardCharsets.UTF_8));
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
