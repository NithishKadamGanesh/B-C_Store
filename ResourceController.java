package com.studyshare.platform.controller;


import com.studyshare.platform.model.Resource;
import com.studyshare.platform.model.User;
import com.studyshare.platform.service.PreviewService;
import com.studyshare.platform.service.ResourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

/**
 * Controller for handling resource uploads, comments, and retrieval in the Study Resource Sharing Platform.
 */
@Controller
public class ResourceController {

    private final ResourceService resourceService;

    @Autowired
    private PreviewService previewService;

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
        return "upload"; // Renders the upload.html view
    }

    /**
     * Handles the resource file upload process.
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
            // Validate the file type
            if (!isValidFileType(file)) {
                throw new IllegalArgumentException("Invalid file type. Please upload a PDF, JPG, or PNG.");
            }

            // Call the service to upload the file
            resourceService.uploadResource(file, title, description, tags);
            redirectAttributes.addFlashAttribute("message", "File uploaded successfully!");

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("message", "Error: " + e.getMessage());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Error uploading the file. Please try again.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Unexpected error: " + e.getMessage());
        }
        return "redirect:/resources"; // Redirect to the list of resources after uploading
    }

    /**
     * Retrieves and displays all uploaded resources.
     * 
     * @param model the {@link Model} to add the resources list to.
     * @return the name of the uploads view template.
     */
    @GetMapping("/resources")
    public String getAllResources(Model model) {
        List<Resource> resources = resourceService.getAllResources();
        model.addAttribute("resources", resources);
        return "resources"; // Renders resources.html from the templates folder
    }
    /**
     * Handles exceptions related to file upload size limits.
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

    /**
     * Validates the file type to ensure it's one of the accepted types.
     * 
     * @param file the file to validate.
     * @return true if the file is a valid type, otherwise false.
     */
    private boolean isValidFileType(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("application/pdf") || contentType.startsWith("image/"));
    }
}
