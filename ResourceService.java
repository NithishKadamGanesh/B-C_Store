package com.studyshare.platform.service;

import com.studyshare.platform.model.Resource;
import com.studyshare.platform.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Service class for managing resources in the Study Resource Sharing Platform.
 * This service handles file uploads to AWS S3, stores metadata in the database,
 * and provides methods to retrieve resources.
 */
@Service
public class ResourceService {

    private final S3Client s3Client;
    private final ResourceRepository resourceRepository;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    public ResourceService(S3Client s3Client, ResourceRepository resourceRepository) {
        this.s3Client = s3Client;
        this.resourceRepository = resourceRepository;
    }

    /**
     * Uploads a resource file to AWS S3 and saves the resource metadata to the database.
     * This method generates a unique file key for the uploaded file, uploads it to S3,
     * and saves the file's URL and metadata in the database. The file is made publicly readable.
     *
     * @param file the file to upload
     * @param title the title of the resource
     * @param description the description of the resource
     * @param tags the tags associated with the resource
     * @throws IOException if an error occurs during file upload
     */
    public void uploadResource(MultipartFile file, String title, String description, String tags) throws IOException {
        // Generate a unique file key using UUID to avoid filename conflicts
        String fileKey = generateUniqueFileKey(file);

        // Upload the file to S3
        try {
            uploadFileToS3(file, fileKey);

            // Create the Resource entity
            Resource resource = new Resource();
            resource.setTitle(title);
            resource.setDescription(description);
            resource.setTags(tags);
            resource.setUploadDate(new Date());

            // Generate S3 URL
            String fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + fileKey;
            resource.setS3Url(fileUrl);

            // Save resource metadata to the database
            resourceRepository.save(resource);
        } catch (Exception e) {
            throw new IOException("Error uploading file to S3 or saving resource to database: " + e.getMessage(), e);
        }
    }

    /**
     * Generates a unique file key for the file by combining a UUID with the original file's extension.
     * This avoids conflicts if files with the same name are uploaded.
     *
     * @param file the file to generate a key for
     * @return the unique file key
     */
    private String generateUniqueFileKey(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        return UUID.randomUUID().toString() + fileExtension;
    }

    /**
     * Uploads the file to AWS S3 using the specified file key.
     *
     * @param file the file to upload
     * @param fileKey the unique key for the file
     * @throws IOException if an error occurs during file upload
     */
    private void uploadFileToS3(MultipartFile file, String fileKey) throws IOException {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .contentType(file.getContentType())
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        // Wrap the file's InputStream in RequestBody
        try {
            PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (Exception e) {
            throw new IOException("Failed to upload file to S3: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all resources stored in the database.
     *
     * @return a list of all resources
     */
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    /**
     * Retrieves a resource by its ID.
     *
     * @param id the ID of the resource
     * @return the resource with the specified ID
     * @throws RuntimeException if the resource is not found
     */
    public Resource getResourceById(Long id) {
        return resourceRepository.findById(id).orElseThrow(() -> new RuntimeException("Resource not found"));
    }
}
