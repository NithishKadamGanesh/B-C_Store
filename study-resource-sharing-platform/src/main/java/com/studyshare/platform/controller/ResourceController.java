package com.studyshare.platform.controller;

import com.studyshare.platform.model.Resource;
import com.studyshare.platform.service.ResourceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

/**
 * Controller for handling resource uploads, retrieval, and error handling in the Study Resource Sharing Platform.
 * <p>
 * This controller provides endpoints for users to upload resources, view all uploaded resources,
 * and manage upload errors. The main pages rendered include the upload form and the list of resources.
 * </p>
 *
 * <p>
 * <strong>Endpoints:</strong>
 * <ul>
 *     <li>{@code /upload} - Renders the upload page.</li>
 *     <li>{@code /resources/upload} - Handles file upload requests.</li>
 *     <li>{@code /resources} - Retrieves and displays a list of uploaded resources.</li>
 * </ul>
 * </p>
 *
 * @see ResourceService
 * @since 1.0
 */
@Controller
public class ResourceController {

    /**
     * Service for handling resource-related operations.
     */
    private final ResourceService resourceService;

    /**
     * Constructs a new ResourceController with the specified ResourceService.
     *
     * @param resourceService the service for managing resources.
     */
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    /**
     * Displays the upload page for submitting new resources.
     *
     * @return the name of the upload view template.
     */
    @GetMapping("/upload")
    public String uploadPage() {
        return "upload"; // Renders upload.html from the templates folder
    }

    /**
     * Handles the resource file upload process.
     * <p>
     * This method accepts a file, title, description, and tags as input. After uploading,
     * it adds a success or failure message to the {@link RedirectAttributes} and redirects
     * to the resources list page.
     * </p>
     * 
     * @param file the resource file to upload.
     * @param title the title of the resource.
     * @param description the description of the resource.
     * @param tags the tags associated with the resource.
     * @param redirectAttributes attributes for a redirect scenario, used to add flash messages.
     * @return the redirect URL for the resources list view.
     */
    @PostMapping("/resources/upload")
    public String uploadResource(@RequestParam("file") MultipartFile file,
                                 @RequestParam("title") String title,
                                 @RequestParam("description") String description,
                                 @RequestParam("tags") String tags,
                                 RedirectAttributes redirectAttributes) {
        try {
            resourceService.uploadResource(file, title, description, tags);
            redirectAttributes.addFlashAttribute("message", "File uploaded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "File upload failed: " + e.getMessage());
        }
        return "redirect:/resources"; // Redirect to the list of resources after uploading
    }

    /**
     * Retrieves and displays all uploaded resources.
     * <p>
     * This method fetches a list of resources from {@link ResourceService} and adds it to
     * the model for display on the uploads page.
     * </p>
     * 
     * @param model the {@link Model} to add the resources list to.
     * @return the name of the uploads view template.
     */
    @GetMapping("/resources")
    public String getAllResources(Model model) {
        List<Resource> resources = resourceService.getAllResources();
        model.addAttribute("resources", resources);
        return "uploads"; // Renders uploads.html from the templates folder
    }

    /**
     * Handles exceptions related to file upload size limits.
     * <p>
     * This method intercepts {@link MaxUploadSizeExceededException} exceptions, typically thrown
     * when a file exceeds the maximum allowed upload size. A flash message is added to indicate
     * the error, and the user is redirected to the upload page.
     * </p>
     * 
     * @param exc the exception triggered by an oversized file upload.
     * @param redirectAttributes attributes for a redirect scenario, used to add flash messages.
     * @return the redirect URL for the upload page with an error message.
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "File too large! Maximum upload size is 100MB.");
        return "redirect:/upload"; // Redirect to the upload page with error message
    }
}
