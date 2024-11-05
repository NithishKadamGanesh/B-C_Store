package com.studyshare.platform.controller;

import com.studyshare.platform.service.PreviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test class for {@link PreviewController}.
 * <p>
 * This class tests the functionality of the {@link PreviewController} endpoints, including successful
 * and error scenarios for file preview requests.
 * </p>
 *
 * @see PreviewController
 * @since 1.0
 */
@WebMvcTest(PreviewController.class)
public class PreviewControllerTest {

    /**
     * MockMvc for simulating HTTP requests to the controller.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * Mock service for managing preview operations.
     */
    @MockBean
    private PreviewService previewService;

    /**
     * The instance of {@link PreviewController} to be tested.
     */
    @InjectMocks
    private PreviewController previewController;

    /**
     * Sets up the test environment by initializing mocks and setting up MockMvc.
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(previewController).build();
    }

    /**
     * Tests the {@code previewResourceFile} endpoint for a successful response.
     * <p>
     * This test mocks the necessary service methods to simulate a successful file retrieval,
     * returning a predefined URL and view name.
     * </p>
     * 
     * @throws Exception if an error occurs during request processing.
     */
    @Test
    public void testPreviewResourceFile_Success() throws Exception {
        
        String expectedView = "preview";
        String mockFileUrl = "https://s3.us-east-2.amazonaws.com/test-bucket/mockfile.pdf";
        when(previewService.getFileKeyById(any(Long.class))).thenReturn("mockfile.pdf");
        when(previewService.generatePresignedUrl("mockfile.pdf")).thenReturn(mockFileUrl);

        // Act
        MvcResult result = mockMvc.perform(get("/preview/1"))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        assertEquals(expectedView, result.getModelAndView().getViewName());
        assertEquals(mockFileUrl, result.getModelAndView().getModel().get("pdfUrl"));
    }

    /**
     * Tests the {@code previewResourceFile} endpoint for a not found error.
     * <p>
     * This test simulates a scenario where the requested resource does not exist, resulting
     * in a 4xx client error.
     * </p>
     * 
     * @throws Exception if an error occurs during request processing.
     */
    @Test
    public void testPreviewResourceFile_NotFound() throws Exception {
        
        when(previewService.getFileKeyById(any(Long.class))).thenThrow(new RuntimeException("Resource not found"));

        // Act & Assert
        mockMvc.perform(get("/preview/999"))
                .andExpect(status().is4xxClientError());
    }
}
