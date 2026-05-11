package com.studyshare.platform.controller;

import com.studyshare.platform.model.Resource;
import com.studyshare.platform.model.User;
import com.studyshare.platform.service.ResourceService;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping("/upload")
    public String uploadPage() {
        return "upload";
    }

    @PostMapping("/resources/upload")
    public String uploadResource(@RequestParam("file") MultipartFile file,
                                 @RequestParam("title") String title,
                                 @RequestParam(value = "description", required = false) String description,
                                 @RequestParam("tags") String tags,
                                 @AuthenticationPrincipal User user,
                                 RedirectAttributes redirectAttributes) {
        try {
            resourceService.uploadResource(file, title, description, tags, user);
            redirectAttributes.addFlashAttribute("success", "Resource uploaded successfully!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/upload";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Upload failed. Please try again.");
            return "redirect:/upload";
        }
        return "redirect:/resources";
    }

    @GetMapping("/resources")
    public String listResources(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "") String search,
                                @RequestParam(defaultValue = "") String tag,
                                Model model) {
        Page<Resource> resourcePage;
        int pageSize = 12;

        if (!search.isBlank()) {
            resourcePage = resourceService.searchResources(search, page, pageSize);
            model.addAttribute("search", search);
        } else if (!tag.isBlank()) {
            resourcePage = resourceService.getResourcesByTag(tag, page, pageSize);
            model.addAttribute("tag", tag);
        } else {
            resourcePage = resourceService.getAllResources(page, pageSize);
        }

        model.addAttribute("resources", resourcePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", resourcePage.getTotalPages());
        model.addAttribute("totalItems", resourcePage.getTotalElements());
        return "resources";
    }

    @GetMapping("/my-resources")
    public String myResources(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("resources", resourceService.getResourcesByUser(user));
        return "my-resources";
    }

    @PostMapping("/resources/{id}/delete")
    public String deleteResource(@PathVariable Long id,
                                 @AuthenticationPrincipal User user,
                                 RedirectAttributes redirectAttributes) {
        try {
            resourceService.deleteResource(id, user);
            redirectAttributes.addFlashAttribute("success", "Resource deleted successfully.");
        } catch (SecurityException e) {
            redirectAttributes.addFlashAttribute("error", "You can only delete your own resources.");
        }
        return "redirect:/my-resources";
    }
}
