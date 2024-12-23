package com.studyshare.platform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for handling the home page.
 * <p>
 * This controller maps the root URL ("/") to the "home" view, providing
 * the entry point for the application.
 * </p>
 */
@Controller
public class HomeController {

    /**
     * Handles requests to the root URL ("/").
     * <p>
     * Maps GET requests to the home page and returns the name of the "home" view template.
     * </p>
     *
     * @return the name of the view template to render, "home".
     */
    @GetMapping("/")
    public String home() {
        return "home";
    }
}