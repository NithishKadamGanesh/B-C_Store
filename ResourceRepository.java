package com.studyshare.platform.repository;

import com.studyshare.platform.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for {@link Resource} entities.
 * <p>
 * This interface extends {@link JpaRepository}, providing CRUD operations
 * and database interaction capabilities for the Resource entity.
 * It enables easy integration with Spring Data JPA, allowing the use of
 * built-in methods for common database operations.
 * </p>
 *
 * <p>
 * The primary key type for {@code Resource} is {@code Long}.
 * </p>
 *
 * @see JpaRepository
 * @see Resource
 */
public interface ResourceRepository extends JpaRepository<Resource, Long> {}
