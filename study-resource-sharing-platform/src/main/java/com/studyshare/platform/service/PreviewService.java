package com.studyshare.platform.service;

import com.studyshare.platform.exception.ResourceNotFoundException;
import com.studyshare.platform.model.Resource;
import com.studyshare.platform.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.time.Duration;

@Service
public class PreviewService {

    private final ResourceRepository resourceRepository;
    private final S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public PreviewService(S3Client s3Client, ResourceRepository resourceRepository) {
        this.s3Client = s3Client;
        this.resourceRepository = resourceRepository;
    }

    public Resource getResourceById(Long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + id));
    }

    public String generateFileUrl(String s3Key) {
        return s3Client.utilities().getUrl(GetUrlRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .build()).toString();
    }
}
