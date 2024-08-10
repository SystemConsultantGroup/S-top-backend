package com.scg.stop.aihub.controller;

import com.scg.stop.aihub.dto.AiHubDatasetRequest;
import com.scg.stop.aihub.dto.AiHubDatasetResponse;
import com.scg.stop.aihub.dto.AiHubModelRequest;
import com.scg.stop.aihub.dto.AiHubModelResponse;
import com.scg.stop.aihub.service.AiHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aihub")
@RequiredArgsConstructor
public class AiHubController {

    private final AiHubService aiHubService;

    @PostMapping("/models")
    public ResponseEntity<Page<AiHubModelResponse>> getAiHubModels(
            @RequestBody AiHubModelRequest request,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<AiHubModelResponse> models = aiHubService.getAiHubModels(request, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(models);
    }

    @PostMapping("/datasets")
    public ResponseEntity<Page<AiHubDatasetResponse>> getAiHubDatasets(
            @RequestBody AiHubDatasetRequest request,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<AiHubDatasetResponse> datasets = aiHubService.getAiHubDatasets(request, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(datasets);
    }
}
