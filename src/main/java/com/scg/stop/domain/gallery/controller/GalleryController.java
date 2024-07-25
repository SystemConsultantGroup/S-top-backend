package com.scg.stop.domain.gallery.controller;

import com.scg.stop.domain.gallery.dto.request.CreateGalleryRequest;
import com.scg.stop.domain.gallery.dto.response.GalleryResponse;
import com.scg.stop.domain.gallery.service.GalleryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/galleries")
public class GalleryController {

    private final GalleryService galleryService;

    // TODO Auth 설정 추가
    @PostMapping
    public ResponseEntity<GalleryResponse> createGallery(@RequestBody @Valid CreateGalleryRequest createGalleryRequest) {
        GalleryResponse galleryResponse = galleryService.createGallery(createGalleryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(galleryResponse);
    }
}
