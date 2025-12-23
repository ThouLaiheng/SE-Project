package com.demo.lms.security.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration for the Library Management System.
 * TEMPORARY: Uses HTTP Basic Authentication (NO JWT).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // Disable CSRF for API testing
                .csrf(csrf -> csrf.disable())

                // Stateless session (still fine)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Authorization rules
                .authorizeHttpRequests(auth -> auth

                        // ===== PUBLIC =====
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/api-docs/**",
                                "/actuator/**"
                        ).permitAll()

                        // ===== CONTACT (USER) =====
                        .requestMatchers(HttpMethod.POST, "/api/contacts").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/contacts/my").hasRole("USER")

                        // ===== CONTACT (ADMIN) =====
                        .requestMatchers("/api/admin/contacts/**").hasRole("ADMIN")

                        // ===== ADMIN =====
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // ===== AUTHENTICATED =====
                        .anyRequest().authenticated()
                )

                // Enable Basic Auth (NO JWT)
                .httpBasic();

        return http.build();
    }
}
