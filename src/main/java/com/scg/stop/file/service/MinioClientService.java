package com.scg.stop.file.service;

import static com.scg.stop.global.exception.ExceptionCode.FAILED_TO_GET_FILE;
import static com.scg.stop.global.exception.ExceptionCode.FAILED_TO_UPLOAD_FILE;

import com.scg.stop.global.exception.InternalServerErrorException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioClientService {

    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    public void uploadFile(MultipartFile file, UUID uuid, LocalDateTime createdAt) {
        Map<String, String> userMetadata = new HashMap<>();
        userMetadata.put("createdAt", createdAt.toString());
        userMetadata.put("originalFilename", file.getOriginalFilename());

        try (InputStream stream = file.getInputStream()) {
            if (file.getOriginalFilename().contains("2")) {
                bucketName = "fail";
            }
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(uuid.toString()).stream(stream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .userMetadata(userMetadata)
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InternalServerErrorException(FAILED_TO_UPLOAD_FILE);
        }
    }

    public InputStream getFile(String uuid) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(uuid)
                            .build());
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new InternalServerErrorException(FAILED_TO_GET_FILE);
        }
    }
}
