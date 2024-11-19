package com.scg.stop.project.controller;

import com.scg.stop.auth.annotation.AuthUser;
import com.scg.stop.project.dto.response.FileResponse;
import com.scg.stop.project.dto.response.ProjectExcelResponse;
import com.scg.stop.project.service.ProjectExcelService;
import com.scg.stop.user.domain.AccessType;
import com.scg.stop.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects/excel")
public class ProjectExcelController {

    private final ProjectExcelService projectExcelService;

    @PostMapping
    public ResponseEntity<ProjectExcelResponse> createProjectExcel(
            @RequestPart("excel") MultipartFile excelFile,
            @RequestPart("thumbnails") List<FileResponse> thumbnails,
            @RequestPart("posters") List<FileResponse> posters,
            @AuthUser(accessType = {AccessType.ADMIN}) User user
    ) {
        ProjectExcelResponse projectExcelResponse = projectExcelService.createProjectExcel(excelFile, thumbnails, posters);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectExcelResponse);
    }
}
