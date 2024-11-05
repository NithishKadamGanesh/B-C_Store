package com.studyshare.platform.service;

import com.studyshare.platform.model.Resource;
import com.studyshare.platform.repository.ResourceRepository;
import com.studyshare.platform.service.PreviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.S3Client;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link PreviewService}.
 * <p>
 * This class tests the functionality of {@link PreviewService} methods,
 * including scenarios for retrieving file keys by ID and handling resource not found exceptions.
 * </p>
 *
 * @see PreviewService
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
class PreviewServiceTest {

    /**
     * Mock repository for managing resource data access.
     */
    @Mock
    private ResourceRepository resourceRepository;

    /**
     * Mock AWS S3 client for handling S3 interactions.
     */
    @Mock
    private S3Client s3Client;

    /**
     * The instance of {@link PreviewService} to be tested.
     */
    @InjectMocks
    private PreviewService previewService;

    /**
     * Initializes mocks before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the {@code getFileKeyById} method for a successful file key retrieval.
     * <p>
     * This test verifies that a valid resource ID retrieves the correct file key from the S3 URL.
     * </p>
     */
    @Test
    void testGetFileKeyById_Success() {
        // Arrange
        Long resourceId = 1L;
        String s3Url = "https://bucket-name.s3.amazonaws.com/mockfile.pdf";
        Resource resource = new Resource();
        resource.setId(resourceId);
        resource.setS3Url(s3Url);

        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));

        // Act
        String fileKey = previewService.getFileKeyById(resourceId);

        // Assert
        assertEquals("mockfile.pdf", fileKey);
    }

    /**
     * Tests the {@code getFileKeyById} method for a resource not found scenario.
     * <p>
     * This test simulates a scenario where a resource with the specified ID does not exist,
     * ensuring that a {@link RuntimeException} is thrown with the expected error message.
     * </p>
     */
    @Test
    void testGetFileKeyById_ResourceNotFound() {
        // Arrange
        Long resourceId = 1L;
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            previewService.getFileKeyById(resourceId);
        });
        assertEquals("Resource not found with ID: " + resourceId, exception.getMessage());
    }
}
