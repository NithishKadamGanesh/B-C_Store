package com.studyshare.platform.controller;

import com.studyshare.platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for handling user registration.
 */
@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    /**
     * Displays the registration page.
     *
     * @return the registration view.
     */
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "registration";  // Return the name of the registration form view (register.html)
    }

    /**
     * Handles user registration requests.
     *
     * @param username the username of the new user.
     * @param password the password of the new user.
     * @param model the model to pass the registration result.
     * @return a redirect or an error page.
     */
    @PostMapping("/register")
    public String registerUser(@RequestParam String username, @RequestParam String password, Model model) {
        try {
            userService.registerUser(username, password);
            model.addAttribute("message", "User registered successfully");
            return "redirect:/login";  // Redirect to login page after successful registration
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "registration";  // Return to the registration page with an error message
        }
    }
}
