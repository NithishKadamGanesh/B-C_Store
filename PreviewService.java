package com.studyshare.platform.service;

import com.studyshare.platform.model.Resource;
import com.studyshare.platform.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;

/**
 * Service class for managing file preview functionality in the Study Resource Sharing Platform.
 * <p>
 * This service handles the retrieval of S3 file keys for resources and generates presigned URLs for temporary access.
 * It interacts with the {@link ResourceRepository} to fetch resource details and uses the {@link S3Client} to generate S3 URLs.
 * </p>
 *
 * @see ResourceRepository
 * @see S3Client
 * @since 1.0
 */
@Service
public class PreviewService {

    /**
     * Repository for accessing resource data.
     */
    @Autowired
    private ResourceRepository resourceRepository;

    /**
     * AWS S3 client for generating presigned URLs and interacting with S3.
     */
    private final S3Client s3Client;

    /**
     * Constructs a new PreviewService with the specified S3 client.
     *
     * @param s3Client the S3 client for AWS S3 interactions.
     */
    public PreviewService(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    /**
     * Retrieves the S3 file key associated with the specified resource ID.
     * <p>
     * This method fetches the resource from the database using the provided ID and extracts
     * the S3 file key from the resource's URL.
     * </p>
     *
     * @param id the ID of the resource.
     * @return the file key for the resource in the S3 bucket.
     * @throws RuntimeException if the resource is not found by the given ID.
     */
    public String getFileKeyById(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found with ID: " + id));
        String s3Url = resource.getS3Url();
        return extractFileKeyFromUrl(s3Url);
    }

    /**
     * Extracts the file key from the full S3 URL.
     * <p>
     * This private helper method extracts the S3 file key from the resource URL by finding the position
     * of ".com/" in the URL and retrieving the remaining part of the URL.
     * </p>
     *
     * @param s3Url the full S3 URL of the resource.
     * @return the extracted file key.
     */
    private String extractFileKeyFromUrl(String s3Url) {
        int index = s3Url.indexOf(".com/") + 5;
        return s3Url.substring(index);
    }

    /**
     * Generates a presigned URL for accessing the specified S3 file key.
     * <p>
     * This method uses the S3 client to create a presigned URL for the given file key,
     * allowing temporary, secure access to the resource.
     * </p>
     *
     * @param fileKey the file key for the resource in the S3 bucket.
     * @return the generated presigned URL as a string.
     */
    public String generatePresignedUrl(String fileKey) {
        return s3Client.utilities().getUrl(GetUrlRequest.builder()
                .bucket("nithishfileupload")
                .key(fileKey)
                .build()).toString();
    }
}
