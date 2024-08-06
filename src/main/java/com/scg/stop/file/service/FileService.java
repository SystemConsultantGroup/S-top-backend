package com.scg.stop.file.service;

import com.scg.stop.file.domain.File;
import com.scg.stop.file.dto.response.FileResponse;
import com.scg.stop.file.repository.FileRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {

    private final FileRepository fileRepository;

    private final MinioClientService minioClientService;

    public FileResponse uploadFile(MultipartFile file) {
        UUID uuid = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        minioClientService.uploadFile(file, uuid, now);
        File fileInfo = File.of(uuid.toString(), file.getOriginalFilename(), file.getContentType());
        fileRepository.save(fileInfo);
        return FileResponse.from(fileInfo);
    }
}
