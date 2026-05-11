package com.studyshare.platform.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "resources", indexes = {
    @Index(name = "idx_resource_title", columnList = "title"),
    @Index(name = "idx_resource_tags", columnList = "tags"),
    @Index(name = "idx_resource_upload_date", columnList = "uploadDate")
})
@Getter @Setter @NoArgsConstructor
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String tags;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type", nullable = false, length = 100)
    private String fileType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "s3_key", nullable = false, length = 512)
    private String s3Key;

    @Column(name = "s3_url", nullable = false, length = 512)
    private String s3Url;

    @Column(name = "upload_date", nullable = false)
    private LocalDateTime uploadDate = LocalDateTime.now();

    @Column(name = "download_count", nullable = false)
    private int downloadCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id", nullable = false)
    private User uploader;

    public String getFormattedFileSize() {
        if (fileSize == null) return "Unknown";
        if (fileSize < 1024) return fileSize + " B";
        if (fileSize < 1024 * 1024) return String.format("%.1f KB", fileSize / 1024.0);
        return String.format("%.1f MB", fileSize / (1024.0 * 1024));
    }

    public boolean isPdf() {
        return fileType != null && fileType.equals("application/pdf");
    }

    public boolean isImage() {
        return fileType != null && fileType.startsWith("image/");
    }
}
