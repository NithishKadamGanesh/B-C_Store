package com.studyshare.platform.controller;

import com.studyshare.platform.model.Resource;
import com.studyshare.platform.service.PreviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/preview")
public class PreviewController {

    private final PreviewService previewService;

    public PreviewController(PreviewService previewService) {
        this.previewService = previewService;
    }

    @GetMapping("/{id}")
    public String previewResource(@PathVariable Long id, Model model) {
        Resource resource = previewService.getResourceById(id);
        String fileUrl = previewService.generateFileUrl(resource.getS3Key());

        model.addAttribute("resource", resource);
        model.addAttribute("fileUrl", fileUrl);
        return "preview";
    }
}
