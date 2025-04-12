package org.micromall.catalog.services;

import java.util.UUID;

import org.micromall.catalog.config.MinioConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    public String uploadImage(MultipartFile file, String folder) throws Exception {
        String fileName = folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucket()).build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucket()).build());
        }

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(minioConfig.getBucket())
                        .object(fileName)
                        .contentType(file.getContentType())
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .build());

        return minioConfig.getUrl() + "/" + minioConfig.getBucket() + "/" + fileName;
    }

        public void deleteImage(String objectName) throws Exception {
        minioClient.removeObject(
            RemoveObjectArgs.builder()
                .bucket(minioConfig.getBucket())
                .object(objectName)
                .build()
        );
    }

}
