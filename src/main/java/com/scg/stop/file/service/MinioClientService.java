package com.scg.stop.file.service;

import com.scg.stop.global.exception.InternalServerErrorException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.scg.stop.global.exception.ExceptionCode.FAILED_TO_GET_FILE;
import static com.scg.stop.global.exception.ExceptionCode.FAILED_TO_UPLOAD_FILE;

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
            String normalizedUuid = Normalizer.normalize(uuid, Normalizer.Form.NFD); // MinIO는 내부적으로 NFD 방식 사용
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(normalizedUuid)
                            .build());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new InternalServerErrorException(FAILED_TO_GET_FILE);
        }
    }
}
