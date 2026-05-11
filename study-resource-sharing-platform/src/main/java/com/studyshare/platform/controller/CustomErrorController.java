package com.studyshare.platform.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorTitle = "Something Went Wrong";
        String errorMessage = "An unexpected error occurred.";

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            switch (statusCode) {
                case 403 -> {
                    errorTitle = "Access Denied";
                    errorMessage = "You don't have permission to access this page.";
                }
                case 404 -> {
                    errorTitle = "Page Not Found";
                    errorMessage = "The page you're looking for doesn't exist.";
                }
                case 500 -> {
                    errorTitle = "Server Error";
                    errorMessage = "Something went wrong on our end. Please try again later.";
                }
            }
        }

        model.addAttribute("errorTitle", errorTitle);
        model.addAttribute("errorMessage", errorMessage);
        return "error";
    }
}
