package com.studyshare.platform.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.studyshare.platform.service.UserService;

/**
 * Security configuration class for the application.
 * <p>
 * This class configures Spring Security settings such as authentication, password encoding,
 * and access restrictions for various endpoints. It extends {@link WebSecurityConfigurerAdapter}
 * to provide custom configurations for securing the application.
 * </p>
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Bean for password encoding.
     * <p>
     * Uses {@link BCryptPasswordEncoder} to hash passwords securely. BCrypt is recommended
     * due to its built-in salt functionality and adaptive nature, making it robust against
     * brute-force attacks.
     * </p>
     * 
     * @return an instance of {@link PasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use BCryptPasswordEncoder for password hashing
    }

    /**
     * Exposes the {@link AuthenticationManager} as a Spring Bean.
     * <p>
     * This is necessary for handling authentication in Spring Security. By exposing this
     * bean, it can be autowired into other parts of the application that require custom
     * authentication logic.
     * </p>
     * 
     * @return an {@link AuthenticationManager} instance.
     * @throws Exception if an error occurs during bean creation.
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * Service for managing user authentication and fetching user details.
     * <p>
     * The {@link UserService} provides the logic for loading users from the database
     * and validating their credentials during the authentication process.
     * </p>
     */
    @Autowired
    private UserService userService;

    /**
     * Password encoder used for encoding and verifying user passwords.
     * <p>
     * This bean is automatically configured to use {@link BCryptPasswordEncoder}, ensuring
     * that passwords are securely hashed and validated.
     * </p>
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Configures the authentication manager with a custom user service and password encoder.
     * <p>
     * This method sets up the {@link AuthenticationManagerBuilder} to use the {@link UserService}
     * for loading user details and the {@link PasswordEncoder} for validating passwords.
     * This ensures that Spring Security uses the application's database for authentication.
     * </p>
     * 
     * @param auth the {@link AuthenticationManagerBuilder} to configure.
     * @throws Exception if an error occurs during configuration.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    /**
     * Configures HTTP security settings for the application.
     * <p>
     * Defines access restrictions, login and logout behavior, and more.
     * <ul>
     *   <li>Public endpoints like login and registration are accessible without authentication.</li>
     *   <li>All other requests require authentication.</li>
     * </ul>
     * </p>
     * 
     * @param http the {@link HttpSecurity} instance to configure.
     * @throws Exception if an error occurs during configuration.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Disables CSRF protection
            .authorizeRequests()
                .antMatchers("/css/**", "/login", "/register").permitAll() // Publicly accessible endpoints
                .anyRequest().authenticated() // All other endpoints require authentication
            .and()
            .formLogin()
                .loginPage("/login") // Specifies the custom login page
                .loginProcessingUrl("/process-login") // Endpoint to handle POST login requests
                .defaultSuccessUrl("/", true) // Redirect after successful login
                .failureUrl("/login?error") // Redirect after failed login with an error query parameter
                .permitAll() // Allows anyone to access the login page
            .and()
            .logout()
                .logoutUrl("/logout") // Specifies the logout endpoint
                .logoutSuccessUrl("/login?logout") // Redirect after successful logout
                .permitAll(); // Allows anyone to access the logout functionality
    }
}