package com.scg.stop.gallery.service;

import static com.scg.stop.global.exception.ExceptionCode.NOT_FOUND_FILE_ID;
import static com.scg.stop.global.exception.ExceptionCode.NOT_FOUND_GALLERY_ID;

import com.scg.stop.file.domain.File;
import com.scg.stop.file.repository.FileRepository;
import com.scg.stop.gallery.domain.Gallery;
import com.scg.stop.gallery.dto.request.GalleryRequest;
import com.scg.stop.gallery.dto.response.GalleryResponse;
import com.scg.stop.gallery.repository.GalleryRepository;
import com.scg.stop.global.exception.BadRequestException;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GalleryService {

    private final GalleryRepository galleryRepository;
    private final FileRepository fileRepository;

    public GalleryResponse createGallery(GalleryRequest request) {
        List<File> files = fileRepository.findByIdIn(request.getFileIds());
        if (files.size() != request.getFileIds().size()) {
            throw new BadRequestException(NOT_FOUND_FILE_ID);
        }

        Gallery gallery = Gallery.of(request.getTitle(), request.getYear(), request.getMonth(), files);
        Gallery savedGallery = galleryRepository.save(gallery);

        return GalleryResponse.from(savedGallery);
    }

    @Transactional(readOnly = true)
    public Page<GalleryResponse> getGalleries(Integer year, Integer month, Pageable pageable) {
        Page<Gallery> galleries = galleryRepository.findGalleries(year, month, pageable);
        return galleries.map(GalleryResponse::from);
    }

    public GalleryResponse getGallery(Long galleryId) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_GALLERY_ID));

        gallery.increaseHitCount();

        return GalleryResponse.from(gallery);
    }

    public GalleryResponse updateGallery(Long galleryId, GalleryRequest request) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_GALLERY_ID));

        List<File> files = fileRepository.findByIdIn(request.getFileIds());
        if (files.size() != request.getFileIds().size()) {
            throw new BadRequestException(NOT_FOUND_FILE_ID);
        }

        gallery.update(request.getTitle(), request.getYear(), request.getMonth(), files);
        Gallery savedGallery = galleryRepository.save(gallery);

        return GalleryResponse.from(savedGallery);
    }

    public void deleteGallery(Long galleryId) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new BadRequestException(NOT_FOUND_GALLERY_ID));
        galleryRepository.delete(gallery);
    }
}