package com.scg.stop.file.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(uuid.toString()).stream(is, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .userMetadata(userMetadata)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }


//    public void getFile(String uuid) {}
}
