   package com.demo.lms.config;

import java.security.SecureRandom;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.demo.lms.Jwt.JwtFilter;

/**
 * Security configuration for the Library Management System.
 * Provides password encoding and basic security settings.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    /**
     * Provides a BCrypt password encoder bean.
     * BCrypt is a strong hashing algorithm suitable for password storage.
     *
     * @return PasswordEncoder instance using BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        SecureRandom secureRandom;
        try{
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (Exception e) {
            secureRandom = new SecureRandom();
        }
        return new BCryptPasswordEncoder(12, secureRandom);
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
                // Static resources & root - public access
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/", "/index.html", "/favicon.ico", "/static/**", "/css/**", "/js/**", "/images/**", "/uploads/**", "/dashboard.html").permitAll()
                .requestMatchers(HttpMethod.GET, "*.html", "*.css", "*.js", "*.png", "*.jpg", "*.ico").permitAll()
                // Swagger/OpenAPI endpoints - public access
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/api-docs/**", "/v3/api-docs/**", "/api/login", "/login", "/register","/api/register").permitAll()
                // Actuator endpoints - public access
                .requestMatchers("/actuator/**").permitAll()
                // API documentation endpoint - public access
                .requestMatchers("/api/docs/**").permitAll()
                // Thymeleaf UI endpoints - public access (allow subpaths)
                .requestMatchers("/createProduct/**", "/createUser/**", "/createReport/**", "/createSetting/**").permitAll()
                .requestMatchers("/users/**").permitAll()
                // Allow all other requests (development mode)
                .anyRequest().permitAll()

            )
            .addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}

