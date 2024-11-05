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

/**
 * Service class for managing resources in the Study Resource Sharing Platform.
 * <p>
 * This service provides functionality for uploading, retrieving, and managing resource files in AWS S3
 * and their corresponding metadata in the database.
 * </p>
 *
 * @see ResourceRepository
 * @see S3Client
 * @since 1.0
 */
@Service
public class ResourceService {

    /**
     * AWS S3 client for handling file uploads.
     */
    private final S3Client s3Client;

    /**
     * Repository for accessing and storing resource data.
     */
    private final ResourceRepository resourceRepository;

    /**
     * The name of the S3 bucket, injected from application properties.
     */
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    /**
     * Constructs a new ResourceService with the specified S3 client and repository.
     *
     * @param s3Client the S3 client for AWS S3 interactions.
     * @param resourceRepository the repository for managing resource entities.
     */
    public ResourceService(S3Client s3Client, ResourceRepository resourceRepository) {
        this.s3Client = s3Client;
        this.resourceRepository = resourceRepository;
    }

    /**
     * Uploads a resource file to AWS S3 and saves the resource metadata to the database.
     * <p>
     * This method generates a unique file key for the uploaded file, uploads it to S3,
     * and saves the file's URL and metadata in the database. The file is made publicly readable.
     * </p>
     *
     * @param file the file to upload.
     * @param title the title of the resource.
     * @param description the description of the resource.
     * @param tags the tags associated with the resource.
     * @throws IOException if an error occurs during file upload.
     */
    public void uploadResource(MultipartFile file, String title, String description, String tags) throws IOException {
        
        String fileKey = file.getOriginalFilename();

        // Upload file to S3
        System.out.println("fileKey " + fileKey);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .contentType(file.getContentType())
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();

        // Wrap the file's InputStream in RequestBody
        PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, 
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        // Create resource entity and save it to the database
        Resource resource = new Resource();
        resource.setTitle(title);
        resource.setDescription(description);
        resource.setTags(tags);
        resource.setUploadDate(new Date());

        // Get the S3 file URL and save it to resource
        String fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + fileKey;
        resource.setS3Url(fileUrl);

        resourceRepository.save(resource);
    }

    /**
     * Retrieves all resources stored in the database.
     *
     * @return a list of all resources.
     */
    public List<Resource> getAllResources() {
        return resourceRepository.findAll();
    }

    /**
     * Retrieves a resource by its ID.
     *
     * @param id the ID of the resource.
     * @return the resource with the specified ID.
     * @throws RuntimeException if the resource is not found.
     */
    public Resource getResourceById(Long id) {
        return resourceRepository.findById(id).orElseThrow(() -> new RuntimeException("Resource not found"));
    }
}
