package com.studyshare.platform.controller;

import com.studyshare.platform.service.PreviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for handling preview requests for study resource files.
 * <p>
 * This controller provides endpoints for viewing study resource files in the browser.
 * By utilizing {@link PreviewService}, it retrieves the file information from S3 and
 * generates a presigned URL for secure access. The generated URL is then passed to the
 * view for rendering.
 * </p>
 * 
 * <p>
 * <strong>Endpoint:</strong> {@code /preview/{id}} - Retrieves a preview of a resource file by ID.
 * </p>
 *
 * @see PreviewService
 * @since 1.0
 */
@Controller
@RequestMapping("/preview")
public class PreviewController {

    /**
     * Service for managing file preview operations.
     */
    @Autowired
    private PreviewService previewService;

    /**
     * Retrieves a preview URL for a study resource file and adds it to the model.
     * <p>
     * This method attempts to locate a file based on the provided resource ID.
     * If found, it generates a presigned URL that allows temporary access to the file in S3.
     * The URL is then added to the model and displayed on the "preview" page. If the file
     * cannot be found, a {@link ResponseStatusException} with status 404 (Not Found) is thrown.
     * </p>
     * 
     * @param id the ID of the study resource to preview.
     * @param model the {@link Model} object for passing data to the view.
     * @return the name of the view template to render the file preview.
     * @throws ResponseStatusException if the resource with the given ID cannot be found.
     */
    @GetMapping("/{id}")
    public String previewResourceFile(@PathVariable Long id, Model model) {
        try {
            String fileKey = previewService.getFileKeyById(id);
            
            String fileUrl = previewService.generatePresignedUrl(fileKey); // Generate S3 URL
           
            model.addAttribute("pdfUrl", fileUrl);
            return "preview";
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found", e);
        }
    }
}
