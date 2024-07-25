package com.scg.stop.domain.gallery.service;

import static com.scg.stop.global.exception.ExceptionCode.NOT_FOUND_FILE_ID;

import com.scg.stop.domain.file.domain.File;
import com.scg.stop.domain.file.dto.response.FileResponse;
import com.scg.stop.domain.file.repository.FileRepository;
import com.scg.stop.domain.gallery.domain.Gallery;
import com.scg.stop.domain.gallery.dto.request.CreateGalleryRequest;
import com.scg.stop.domain.gallery.dto.response.GalleryResponse;
import com.scg.stop.domain.gallery.repository.GalleryRepository;
import com.scg.stop.global.exception.BadRequestException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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

        Gallery gallery = Gallery.of(request, files);
        Gallery savedGallery = galleryRepository.save(gallery);

        List<FileResponse> fileResponses = files.stream()
                .map(FileResponse::from)
                .collect(Collectors.toList());
        return GalleryResponse.of(savedGallery, fileResponses);
    }
}
