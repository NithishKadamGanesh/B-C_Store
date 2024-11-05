package com.studyshare.platform.controller;

import com.studyshare.platform.model.Resource;
import com.studyshare.platform.service.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link ResourceController}.
 * <p>
 * This class contains tests for each endpoint and method within the {@link ResourceController},
 * including scenarios for successful and unsuccessful resource uploads, retrieval of all resources,
 * and file size exception handling.
 * </p>
 *
 * @see ResourceController
 * @since 1.0
 */
class ResourceControllerTest {

    /**
     * Mock service for managing resource operations.
     */
    @Mock
    private ResourceService resourceService;

    /**
     * Mock model for passing attributes to views.
     */
    @Mock
    private Model model;

    /**
     * Mock file for testing file upload functionality.
     */
    @Mock
    private MultipartFile file;

    /**
     * Mock attributes for redirect scenarios.
     */
    @Mock
    private RedirectAttributes redirectAttributes;

    /**
     * The instance of {@link ResourceController} to be tested, injected with mocks.
     */
    @InjectMocks
    private ResourceController resourceController;

    /**
     * Sets up the test environment by initializing mocks before each test.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the {@code uploadPage} method to ensure it returns the correct view name for the upload page.
     */
    @Test
    void testUploadPage() {
        String viewName = resourceController.uploadPage();
        assertEquals("upload", viewName);
    }

    /**
     * Tests the {@code uploadResource} method for a successful upload.
     * <p>
     * This test verifies that the upload process completes without errors,
     * and that a success message is added to the redirect attributes.
     * </p>
     *
     * @throws Exception if an error occurs during the upload process.
     */
    @Test
    void testUploadResource_Success() throws Exception {
        String title = "Test Title";
        String description = "Test Description";
        String tags = "test, tags";

        doNothing().when(resourceService).uploadResource(file, title, description, tags);

        String result = resourceController.uploadResource(file, title, description, tags, redirectAttributes);

        verify(resourceService, times(1)).uploadResource(file, title, description, tags);
        verify(redirectAttributes, times(1)).addFlashAttribute("message", "File uploaded successfully!");
        assertEquals("redirect:/resources", result);
    }

    /**
     * Tests the {@code uploadResource} method for an upload failure.
     * <p>
     * This test simulates a file upload error and verifies that an error message
     * is added to the redirect attributes.
     * </p>
     *
     * @throws Exception if an error occurs during the upload process.
     */
    @Test
    void testUploadResource_Failure() throws Exception {
        String title = "Test Title";
        String description = "Test Description";
        String tags = "test, tags";

        doThrow(new IOException("File upload error")).when(resourceService).uploadResource(file, title, description, tags);

        String result = resourceController.uploadResource(file, title, description, tags, redirectAttributes);

        verify(resourceService, times(1)).uploadResource(file, title, description, tags);
        verify(redirectAttributes, times(1)).addFlashAttribute("message", "File upload failed: File upload error");
        assertEquals("redirect:/resources", result);
    }

    /**
     * Tests the {@code getAllResources} method to ensure it retrieves and sets the correct resources
     * in the model and returns the correct view name.
     */
    @Test
    void testGetAllResources() {
        Resource resource1 = new Resource();
        resource1.setTitle("Resource 1");
        Resource resource2 = new Resource();
        resource2.setTitle("Resource 2");

        List<Resource> resources = Arrays.asList(resource1, resource2);

        when(resourceService.getAllResources()).thenReturn(resources);

        String viewName = resourceController.getAllResources(model);

        verify(resourceService, times(1)).getAllResources();
        verify(model, times(1)).addAttribute("resources", resources);
        assertEquals("uploads", viewName);
    }

    /**
     * Tests the {@code handleMaxSizeException} method to ensure it handles file size exceptions
     * appropriately by adding an error message to the redirect attributes.
     */
    @Test
    void testHandleMaxSizeException() {
        MaxUploadSizeExceededException exception = new MaxUploadSizeExceededException(100000L);
        String result = resourceController.handleMaxSizeException(exception, redirectAttributes);

        verify(redirectAttributes, times(1)).addFlashAttribute("message", "File too large! Maximum upload size is 100MB.");
        assertEquals("redirect:/upload", result);
    }
}
