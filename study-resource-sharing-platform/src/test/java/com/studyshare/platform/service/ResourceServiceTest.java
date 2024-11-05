package com.studyshare.platform.service;

import com.studyshare.platform.model.Resource;
import com.studyshare.platform.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link ResourceService}.
 * <p>
 * This class tests the functionality of {@link ResourceService} methods, including resource upload,
 * retrieval of all resources, and fetching a resource by its ID.
 * </p>
 *
 * @see ResourceService
 * @since 1.0
 */
class ResourceServiceTest {

    /**
     * Mock S3 client for handling AWS S3 interactions.
     */
    @Mock
    private S3Client s3Client;

    /**
     * Mock repository for managing resource data access.
     */
    @Mock
    private ResourceRepository resourceRepository;

    /**
     * Mock file for simulating file uploads.
     */
    @Mock
    private MultipartFile file;

    /**
     * The instance of {@link ResourceService} to be tested, injected with mocks.
     */
    @InjectMocks
    private ResourceService resourceService;

    /**
     * AWS S3 bucket name, injected from application properties.
     */
    @Value("${aws.s3.bucketName}")
    private String bucketName;

    /**
     * Initializes mocks before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the {@code uploadResource} method for successful file upload and database save.
     * <p>
     * This test simulates a file upload to S3 and verifies that the resource metadata is saved
     * in the database. It checks interactions with both S3 and the database repository.
     * </p>
     *
     * @throws IOException if an error occurs during the file upload process.
     */
    @Test
    void testUploadResource() throws IOException {
        
        when(file.getOriginalFilename()).thenReturn("test-file.pdf");
        when(file.getContentType()).thenReturn("application/pdf");
        when(file.getSize()).thenReturn(1024L);
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[1024]));

        Resource resource = new Resource();
        resource.setId(1L);
        resource.setTitle("Test Title");
        resource.setDescription("Test Description");
        resource.setTags("Test Tag");
        resource.setUploadDate(new Date());
        resource.setS3Url("https://" + bucketName + ".s3.amazonaws.com/test-file.pdf");

        when(resourceRepository.save(any(Resource.class))).thenReturn(resource);

        // Act
        resourceService.uploadResource(file, "Test Title", "Test Description", "Test Tag");

        // Assert
        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
        verify(resourceRepository, times(1)).save(any(Resource.class));

        assertEquals(Long.valueOf(1L), resource.getId());
    }

    /**
     * Tests the {@code getAllResources} method to ensure it retrieves all resources.
     * <p>
     * This test verifies that the service retrieves a list of resources from the repository
     * and that the repository method is called once.
     * </p>
     */
    @Test
    public void testGetAllResources() {
        
        List<Resource> resources = new ArrayList<>();
        resources.add(new Resource()); // Adding a single resource to match the expected result
        when(resourceRepository.findAll()).thenReturn(resources);

        // Act
        List<Resource> result = resourceService.getAllResources();

        // Assert
        assertEquals(1, result.size());  // Expecting 1 item in the list
        verify(resourceRepository, times(1)).findAll();
    }

    /**
     * Tests the {@code getResourceById} method for a successful resource retrieval by ID.
     * <p>
     * This test simulates fetching a resource by ID, verifies the returned resource's properties,
     * and ensures that the repository method is called once.
     * </p>
     */
    @Test
    public void testGetResourceById_Success() {
        
        Long resourceId = 1L;
        Resource resource = new Resource();
        resource.setId(resourceId);
        resource.setTitle("Sample Title");
        resource.setDescription("Sample Description");
        resource.setTags("tag1, tag2");
        resource.setS3Url("https://bucket.s3.amazonaws.com/mockfile.pdf");
        
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));
        
        // Act
        Resource result = resourceService.getResourceById(resourceId);
        
        // Assert
        assertEquals(resourceId, result.getId());
        assertEquals("Sample Title", result.getTitle());
        verify(resourceRepository, times(1)).findById(resourceId);
    }
}
