package com.studyshare.platform.controller;

import com.studyshare.platform.model.User;
import com.studyshare.platform.repository.ResourceRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ResourceRepository resourceRepository;

    public HomeController(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("totalResources", resourceRepository.count());
        model.addAttribute("myResources", resourceRepository.countByUploader(user));
        return "home";
    }
}
