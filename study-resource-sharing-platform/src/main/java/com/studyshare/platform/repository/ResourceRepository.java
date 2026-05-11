package com.studyshare.platform.repository;

import com.studyshare.platform.model.Resource;
import com.studyshare.platform.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {

    Page<Resource> findAllByOrderByUploadDateDesc(Pageable pageable);

    List<Resource> findByUploaderOrderByUploadDateDesc(User uploader);

    @Query("SELECT r FROM Resource r WHERE " +
           "LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(r.tags) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Resource> search(@Param("query") String query, Pageable pageable);

    @Query("SELECT r FROM Resource r WHERE LOWER(r.tags) LIKE LOWER(CONCAT('%', :tag, '%'))")
    Page<Resource> findByTag(@Param("tag") String tag, Pageable pageable);

    long countByUploader(User uploader);
}
