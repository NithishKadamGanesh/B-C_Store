package com.studyshare.platform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Custom error controller for handling application errors.
 * <p>
 * This controller overrides the default error handling behavior in Spring Boot by implementing
 * {@link org.springframework.boot.web.servlet.error.ErrorController}. It directs users to a custom error
 * page template when an error occurs.
 * </p>
 *
 * <p>
 * This setup allows for additional logging or custom error handling, which can be extended
 * based on specific requirements.
 * </p>
 *
 * @see org.springframework.boot.web.servlet.error.ErrorController
 * @since 1.0
 */
@Controller
public class CustomErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    /**
     * Handles HTTP errors by redirecting to a custom error page.
     * <p>
     * This method intercepts requests to the `/error` path, allowing for custom error processing.
     * The method returns the name of the error page template (without the `.html` extension),
     * which should be located in the view templates directory.
     * </p>
     * 
     * @param request the {@link HttpServletRequest} containing error details.
     * @return the name of the custom error view template.
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // You can add logging or custom error handling logic here
        return "customError"; // Return the name of the error page template (without the .html extension)
    }
}
