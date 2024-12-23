package com.studyshare.platform.repository;

import com.studyshare.platform.model.Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Role entity.
 * <p>
 * This interface provides data access methods for the Role entity,
 * leveraging Spring Data JPA to interact with the database.
 * </p>
 *
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    /**
     * Finds a Role by its name.
     * <p>
     * This method queries the database to retrieve a Role entity with the specified name.
     * If no Role is found, it returns an Optional.empty().
     * </p>
     *
     * @param name the name of the role to search for.
     * @return an Optional containing the Role entity if found, or Optional.empty() otherwise.
     */
    Optional<Role> findByName(String name);
}