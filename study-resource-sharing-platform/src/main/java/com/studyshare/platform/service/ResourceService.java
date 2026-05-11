package com.studyshare.platform.service;

import com.studyshare.platform.exception.ResourceNotFoundException;
import com.studyshare.platform.model.Resource;
import com.studyshare.platform.model.User;
import com.studyshare.platform.repository.ResourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ResourceService {

    private static final Logger log = LoggerFactory.getLogger(ResourceService.class);
    private static final Set<String> ALLOWED_TYPES = Set.of(
            "application/pdf", "image/jpeg", "image/png", "image/gif",
            "image/webp", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.ms-powerpoint",
            "application/vnd.openxmlformats-officedocument.presentationml.presentation",
            "text/plain"
    );

    private final S3Client s3Client;
    private final ResourceRepository resourceRepository;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    public ResourceService(S3Client s3Client, ResourceRepository resourceRepository) {
        this.s3Client = s3Client;
        this.resourceRepository = resourceRepository;
    }

    public Resource uploadResource(MultipartFile file, String title, String description, String tags, User uploader) throws IOException {
        validateFile(file);

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String s3Key = "uploads/" + UUID.randomUUID() + extension;

        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(s3Key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
            log.info("Uploaded file to S3: {}", s3Key);
        } catch (Exception e) {
            throw new IOException("Failed to upload file to S3: " + e.getMessage(), e);
        }

        Resource resource = new Resource();
        resource.setTitle(title.trim());
        resource.setDescription(description != null ? description.trim() : "");
        resource.setTags(tags.trim().toLowerCase());
        resource.setFileName(originalFilename);
        resource.setFileType(file.getContentType());
        resource.setFileSize(file.getSize());
        resource.setS3Key(s3Key);
        resource.setS3Url(buildS3Url(s3Key));
        resource.setUploadDate(LocalDateTime.now());
        resource.setUploader(uploader);

        return resourceRepository.save(resource);
    }

    public Page<Resource> getAllResources(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return resourceRepository.findAllByOrderByUploadDateDesc(pageable);
    }

    public Page<Resource> searchResources(String query, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return resourceRepository.search(query.trim(), pageable);
    }

    public Page<Resource> getResourcesByTag(String tag, int page, int size) {
        return resourceRepository.findByTag(tag.trim(), PageRequest.of(page, size));
    }

    public Resource getResourceById(Long id) {
        return resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + id));
    }

    public List<Resource> getResourcesByUser(User user) {
        return resourceRepository.findByUploaderOrderByUploadDateDesc(user);
    }

    public void deleteResource(Long id, User currentUser) {
        Resource resource = getResourceById(id);
        if (!resource.getUploader().getId().equals(currentUser.getId())) {
            throw new SecurityException("You can only delete your own resources");
        }

        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(resource.getS3Key())
                    .build());
            log.info("Deleted file from S3: {}", resource.getS3Key());
        } catch (Exception e) {
            log.error("Failed to delete S3 object: {}", e.getMessage());
        }

        resourceRepository.delete(resource);
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException(
                "Invalid file type: " + contentType + ". Allowed types: PDF, images, Word, PowerPoint, plain text.");
        }
    }

    private String buildS3Url(String s3Key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, s3Key);
    }
}
