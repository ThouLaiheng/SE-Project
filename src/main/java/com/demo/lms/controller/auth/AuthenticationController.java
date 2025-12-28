package com.demo.lms.controller.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.demo.lms.dto.request.LoginRequest;
import com.demo.lms.dto.response.LoginResponse;

@Controller
public class AuthenticationController {

    private final PasswordEncoder encoder;

    public AuthenticationController(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @PostMapping("/api/login")
    public ResponseEntity<LoginResponse> loginAPI(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(
            new LoginResponse(loginRequest.getEmail(), encoder.encode(loginRequest.getPassword())));
    }
}
