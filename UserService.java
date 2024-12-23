package com.studyshare.platform.service;

import com.studyshare.platform.model.Role;
import com.studyshare.platform.model.User;
import com.studyshare.platform.repository.RoleRepository;
import com.studyshare.platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing user operations like registration and fetching all users.
 */
@Service
public class UserService implements UserDetailsService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registers a new user with the provided username and password.
     *
     * @param username the username of the new user.
     * @param password the password of the new user.
     * @return the created User entity.
     */
    @Transactional
    public User registerUser(String username, String password) {
        validateUsernameAndPassword(username, password);

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        Role userRole = createOrFetchRole("USER");
        user.addRole(userRole);

        return userRepository.save(user);
    }

    /**
     * Loads a user by username.
     *
     * @param username the username of the user.
     * @return the User entity.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return user;
    }
    /**
     * Helper method to fetch an existing role or create a new one if it doesn't exist.
     *
     * @param roleName the name of the role.
     * @return the Role entity.
     */
    private Role createOrFetchRole(String roleName) {
        return roleRepository.findByName(roleName).orElseGet(() -> {
            Role newRole = new Role(roleName);
            return roleRepository.save(newRole);
        });
    }

    /**
     * Validates the username and password for registration.
     *
     * @param username the username to validate.
     * @param password the password to validate.
     */
    private void validateUsernameAndPassword(String username, String password) {
        if (username == null || username.trim().isEmpty() || username.length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 characters long");
        }
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
    }

    /**
     * Custom exception class for user not found scenarios.
     */
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String message) {
            super(message);
        }
    }
}
