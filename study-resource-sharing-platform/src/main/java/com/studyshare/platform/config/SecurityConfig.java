package com.studyshare.platform.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Security configuration class for the Study Resource Sharing Platform application.
 * <p>
 * This class enables web security settings for the application by extending
 * {@link WebSecurityConfigurerAdapter}. In this configuration, all HTTP requests are
 * permitted without authentication, and Cross-Site Request Forgery (CSRF) protection is disabled.
 * </p>
 *
 * <p>
 * <strong>Note:</strong> This setup is suitable for prototyping and development environments.
 * For production environments, it is recommended to enable authentication and CSRF protection.
 * </p>
 *
 * @see WebSecurityConfigurerAdapter
 * @see HttpSecurity
 * @since 1.0
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Configures HTTP security for the application.
     * <p>
     * This method permits all incoming HTTP requests without requiring authentication
     * and disables CSRF protection to simplify requests. It is intended for a prototype
     * environment where strict security is not enforced.
     * </p>
     * 
     * @param http the {@link HttpSecurity} instance for configuring web security.
     * @throws Exception if an error occurs during configuration.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http
            .authorizeRequests()
            .anyRequest().permitAll() // Allow all requests without authentication
            .and()
            .csrf().disable();
    }
}
