package com.scg.stop.domain.gallery.service;

import static com.scg.stop.global.exception.ExceptionCode.NOT_FOUND_FILE_ID;

import com.scg.stop.domain.file.domain.File;
import com.scg.stop.domain.file.dto.response.FileResponse;
import com.scg.stop.domain.file.repository.FileRepository;
import com.scg.stop.domain.gallery.domain.Gallery;
import com.scg.stop.domain.gallery.dto.request.CreateGalleryRequest;
import com.scg.stop.domain.gallery.dto.request.UpdateGalleryRequest;
import com.scg.stop.domain.gallery.dto.response.GalleryResponse;
import com.scg.stop.domain.gallery.repository.GalleryRepository;
import com.scg.stop.global.exception.BadRequestException;
import java.util.List;
import java.util.stream.Collectors;
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

    public GalleryResponse createGallery(CreateGalleryRequest request) {
        List<File> files = fileRepository.findByIdIn(request.getFileIds());
        if (files.size() != request.getFileIds().size()) {
            throw new BadRequestException(NOT_FOUND_FILE_ID);
        }

        Gallery gallery = Gallery.of(request.getTitle(), request.getContent(), request.getYear(), request.getMonth(), files);
        Gallery savedGallery = galleryRepository.save(gallery);

        List<FileResponse> fileResponses = files.stream()
                .map(FileResponse::from)
                .collect(Collectors.toList());
        return GalleryResponse.of(savedGallery, fileResponses);
    }

    @Transactional(readOnly = true)
    public Page<GalleryResponse> getGalleries(Integer year, Integer month, Pageable pageable) {
        Page<Gallery> galleries = galleryRepository.findGalleries(year, month, pageable);
        return galleries.map(this::entityToGalleryResponse);
    }

    @Transactional(readOnly = true)
    public GalleryResponse getGallery(Long galleryId) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new IllegalArgumentException("요청한 ID(" + galleryId + ")에 해당하는 갤러리가 없습니다."));
        return entityToGalleryResponse(gallery);
    }

    private GalleryResponse entityToGalleryResponse(Gallery gallery) {
        List<FileResponse> fileResponses = gallery.getFiles().stream()
                .map(FileResponse::from)
                .collect(Collectors.toList());
        return GalleryResponse.of(gallery, fileResponses);
    }

    public GalleryResponse updateGallery(Long galleryId, UpdateGalleryRequest request) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new IllegalArgumentException("요청한 ID(" + galleryId + ")에 해당하는 갤러리가 없습니다."));

        List<File> files = fileRepository.findByIdIn(request.getFileIds());
        if (files.size() != request.getFileIds().size()) {
            throw new BadRequestException(NOT_FOUND_FILE_ID);
        }

        gallery.update(request.getTitle(), request.getContent(), request.getYear(), request.getMonth(), files);
        galleryRepository.save(gallery);

        List<FileResponse> fileResponses = files.stream()
                .map(FileResponse::from)
                .collect(Collectors.toList());
        return GalleryResponse.of(gallery, fileResponses);
    }

    public void deleteGallery(Long galleryId) {
        Gallery gallery = galleryRepository.findById(galleryId)
                .orElseThrow(() -> new IllegalArgumentException("요청한 ID(" + galleryId + ")에 해당하는 갤러리가 없습니다."));
        galleryRepository.delete(gallery);
    }
}