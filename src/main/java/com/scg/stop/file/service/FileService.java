package com.scg.stop.file.service;

import static com.scg.stop.global.exception.ExceptionCode.FILE_NOT_FOUND;
import static com.scg.stop.global.exception.ExceptionCode.INVALID_FILE_PATH;

import com.scg.stop.file.domain.File;
import com.scg.stop.file.dto.response.FileResponse;
import com.scg.stop.file.repository.FileRepository;
import com.scg.stop.global.exception.BadRequestException;
import com.scg.stop.global.exception.InternalServerErrorException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final FileRepository fileRepository;

    private final MinioClientService minioClientService;

    @Transactional
    public List<FileResponse> uploadFiles(List<MultipartFile> files) {
        LocalDateTime now = LocalDateTime.now();
        List<File> fileInfos = new ArrayList<>();
        for (MultipartFile file : files) {
            UUID uuid = UUID.randomUUID();
            minioClientService.uploadFile(file, uuid, now);
            File fileInfo = File.of(uuid.toString(), file.getOriginalFilename(), file.getContentType());
            fileInfos.add(fileInfo);
        }
        fileRepository.saveAll(fileInfos);

        List<FileResponse> fileResponses = new ArrayList<>();
        for (File fileInfo : fileInfos) {
            FileResponse fileResponse = FileResponse.from(fileInfo);
            fileResponses.add(fileResponse);
        }
        return fileResponses;
    }

    public InputStream getFile(Long fileId) {
        File file = fileRepository.findById(fileId).orElseThrow(() -> new BadRequestException(FILE_NOT_FOUND));
        return minioClientService.getFile(file.getUuid());
    }

    public File getFileMetadata(Long fileId) {
        return fileRepository.findById(fileId).orElseThrow(() -> new BadRequestException(FILE_NOT_FOUND));
    }

    public Resource getLocalFile(String directoryPath, String fileName) {
        ClassPathResource file = new ClassPathResource(directoryPath + fileName);
        if (!file.exists()) {
            throw new InternalServerErrorException(FILE_NOT_FOUND);
        }
        return file;
    }
}
