package com.scg.stop.project.service;

import com.scg.stop.project.dto.response.ProjectExcelResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProjectExcelService {
    public ProjectExcelResponse createProjectExcel(MultipartFile excelFile) {
        return null;
    }
}
