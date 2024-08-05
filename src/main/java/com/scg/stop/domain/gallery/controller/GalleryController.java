package com.scg.stop.domain.gallery.controller;

import com.scg.stop.domain.gallery.dto.request.GalleryRequest;
import com.scg.stop.domain.gallery.dto.response.GalleryResponse;
import com.scg.stop.domain.gallery.service.GalleryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/galleries")
public class GalleryController {

    private final GalleryService galleryService;

    // TODO Auth 설정 추가
    @PostMapping
    public ResponseEntity<GalleryResponse> createGallery(@RequestBody @Valid GalleryRequest galleryRequest) {
        GalleryResponse galleryResponse = galleryService.createGallery(galleryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(galleryResponse);
    }

    @GetMapping
    public ResponseEntity<Page<GalleryResponse>> getGalleries(
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<GalleryResponse> galleries = galleryService.getGalleries(year, month, pageable);
        return ResponseEntity.ok(galleries);
    }

    @GetMapping("/{galleryId}")
    public ResponseEntity<GalleryResponse> getGallery(@PathVariable("galleryId") Long galleryId) {
        GalleryResponse galleryResponse = galleryService.getGallery(galleryId);
        return ResponseEntity.ok(galleryResponse);
    }

    // TODO Auth 설정 추가
    @PutMapping("/{galleryId}")
    public ResponseEntity<GalleryResponse> updateGallery(
            @PathVariable("galleryId") Long galleryId,
            @RequestBody @Valid GalleryRequest galleryRequest) {
        GalleryResponse galleryResponse = galleryService.updateGallery(galleryId, galleryRequest);
        return ResponseEntity.ok(galleryResponse);
    }

    // TODO Auth 설정 추가
    @DeleteMapping("/{galleryId}")
    public ResponseEntity<Void> deleteGallery(@PathVariable("galleryId") Long galleryId) {
        galleryService.deleteGallery(galleryId);
        return ResponseEntity.noContent().build();
    }
}
