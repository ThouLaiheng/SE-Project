package com.demo.lms.controller.auth;

import com.demo.lms.Jwt.JwtUtil;
import com.demo.lms.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.demo.lms.dto.request.LoginRequest;
import com.demo.lms.dto.request.RegisterRequest;
import com.demo.lms.dto.response.AuthResponse;
import com.demo.lms.model.entity.User;
import com.demo.lms.exception.UserAlreadyExistsException;
import java.util.Map;


@RestController
public class AuthenticationController {

    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtUtil jwtUtil;

    AuthenticationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/api/login")
        public ResponseEntity<?> loginAPI(@RequestBody LoginRequest loginRequest) {
        String email = loginRequest.getEmail() == null ? null : loginRequest.getEmail().trim().toLowerCase();
        return userRepository.findByEmail(email)
            .filter(u -> encoder.matches(loginRequest.getPassword(), u.password))
            .<ResponseEntity<?>>map(u -> ResponseEntity.ok(new AuthResponse(u.email, jwtUtil.generateToken(u.email))))
            .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid credentials")));
        }
    @PostMapping("/api/register")
    public ResponseEntity<?> registerAPI(@Valid @RequestBody RegisterRequest registerRequest) {
        String email = registerRequest.getEmail() == null ? null : registerRequest.getEmail().trim().toLowerCase();
        // Check for duplicate email (normalized)
        if (email != null && userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistsException("User already exists with email: " + email);
        }
        User user = new User();
        // Ensure non-null name to satisfy DB constraint
        user.setName(email);
        user.email = email;
        user.password = encoder.encode(registerRequest.getPassword());
        user.role = "USER";
        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail());
        return new ResponseEntity<>(new AuthResponse(user.getEmail(), token), HttpStatus.CREATED);
    }
    
    

    

    
}
