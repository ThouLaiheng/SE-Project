   package com.demo.lms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the Library Management System.
 * Provides password encoding and basic security settings.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Provides a BCrypt password encoder bean.
     * BCrypt is a strong hashing algorithm suitable for password storage.
     *
     * @return PasswordEncoder instance using BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures HTTP security.
     * Currently permits all requests for development purposes.
     * In production, this should be configured with proper authentication.
     * Explicitly allows Swagger/OpenAPI documentation endpoints.
     *
     * @param http HttpSecurity configuration object
     * @return SecurityFilterChain
     * @throws Exception if configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for API development
            .authorizeHttpRequests(auth -> auth
                // Swagger/OpenAPI endpoints - public access
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/api-docs/**", "/v3/api-docs/**").permitAll()
                // Actuator endpoints - public access
                .requestMatchers("/actuator/**").permitAll()
                // API documentation endpoint - public access
                .requestMatchers("/api/docs/**").permitAll()
                // Allow all other requests (development mode)
                .anyRequest().permitAll()
            );

        return http.build();
    }
}

