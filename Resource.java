package com.studyshare.platform.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Entity representing a study resource in the Study Resource Sharing Platform.
 * <p>
 * Each resource has a title, description, tags, an upload date, and a URL pointing
 * to its location on Amazon S3. This entity is mapped to the {@code resources} table in the database.
 * </p>
 *
 * @see javax.persistence.Entity
 * @since 1.0
 */
@Entity
@Table(name = "resources")
public class Resource {

    /**
     * Unique identifier for the resource.
     * Generated automatically using an identity strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Title of the resource. This field is required.
     */
    @Column(nullable = false)
    private String title;

    /**
     * Description of the resource, up to 500 characters in length.
     */
    @Column(length = 500)
    private String description;

    /**
     * Tags associated with the resource, used for categorization and searching.
     * This field is required.
     */
    @Column(nullable = false)
    private String tags;

    /**
     * Date when the resource was uploaded.
     * Mapped as a timestamp in the database and marked as a required field.
     */
    @Column(name = "upload_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadDate;

    /**
     * URL pointing to the resource's location in Amazon S3.
     * This field is required and has a maximum length of 512 characters.
     */
    @Column(name = "s3_url", nullable = false, length = 512)
    private String s3Url;
    /**
     * Gets the unique ID of the resource.
     *
     * @return the resource ID.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the unique ID of the resource.
     *
     * @param id the ID to set for the resource.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the title of the resource.
     *
     * @return the resource title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the resource.
     *
     * @param title the title to set for the resource.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description of the resource.
     *
     * @return the resource description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the resource.
     *
     * @param description the description to set for the resource.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the tags associated with the resource.
     *
     * @return the resource tags.
     */
    public String getTags() {
        return tags;
    }

    /**
     * Sets the tags associated with the resource.
     *
     * @param tags the tags to set for the resource.
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * Gets the upload date of the resource.
     *
     * @return the upload date.
     */
    public Date getUploadDate() {
        return uploadDate;
    }

    /**
     * Sets the upload date of the resource.
     *
     * @param uploadDate the upload date to set for the resource.
     */
    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    /**
     * Gets the S3 URL of the resource.
     *
     * @return the S3 URL.
     */
    public String getS3Url() {
        return s3Url;
    }

    /**
     * Sets the S3 URL of the resource.
     *
     * @param s3Url the S3 URL to set for the resource.
     */
    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }
    /**
     * Returns a string representation of the Resource object.
     *
     * @return a string representation of the Resource.
     */
    @Override
    public String toString() {
        return "Resource{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", tags='" + tags + '\'' +
                ", uploadDate=" + uploadDate +
                ", s3Url='" + s3Url + '\'' 
;
    }
}
