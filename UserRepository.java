package com.studyshare.platform.repository;

import com.studyshare.platform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repository for managing User entities.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by username.
     *
     * @param username the username of the user.
     * @return an Optional of User.
     */
    Optional<User> findByUsername(String username);
}
