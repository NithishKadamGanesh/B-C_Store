package com.studyshare.platform.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Unit test class for {@link CustomErrorController}.
 * <p>
 * This class contains tests to verify the functionality of custom error handling
 * provided by the {@link CustomErrorController}.
 * </p>
 *
 * @see CustomErrorController
 * @since 1.0
 */
class CustomErrorControllerTest {

    /**
     * The instance of {@link CustomErrorController} to be tested, injected with mocks.
     */
    @InjectMocks
    private CustomErrorController customErrorController;

    /**
     * Initializes mocks before each test.
     * <p>
     * This setup method opens mocks for dependency injection, preparing the test environment.
     * </p>
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the {@code handleError} method to ensure it returns the correct error view name.
     * <p>
     * This test verifies that the {@link CustomErrorController#handleError(HttpServletRequest)} method
     * returns the expected view name {@code "customError"} when an error occurs.
     * </p>
     */
    @Test
    void testHandleError() {
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);

        String viewName = customErrorController.handleError(mockRequest);

        // Assert that it returns the expected error view name
        assertEquals("customError", viewName, "Should return the custom error view name");
    }
}
