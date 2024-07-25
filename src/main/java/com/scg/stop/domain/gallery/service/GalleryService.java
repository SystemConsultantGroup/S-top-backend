package com.scg.stop.domain.gallery.service;

import static org.hamcrest.Matchers.any;

import com.scg.stop.domain.gallery.dto.request.CreateGalleryRequest;
import com.scg.stop.domain.gallery.dto.response.GalleryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class GalleryService {

    public GalleryResponse createGallery(CreateGalleryRequest request) {
        return (GalleryResponse) any(GalleryResponse.class);
    }
}
