package com.demo.lms.controller.user;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.demo.lms.dto.request.LoginRequest;
import com.demo.lms.dto.response.LoginResponse;


@Controller
public class AuthenticationController {
    @Autowired
    private PasswordEncoder encoder;

    @PostMapping("/api/login")
    public ResponseEntity<?> loginAPI(@RequestBody LoginRequest loginRequest) {
        //TODO: process POST request
        
        return new ResponseEntity<>(
            new LoginResponse(loginRequest.getEmail(), encoder.encode(loginRequest.getPassword())), null, 200);
    }
    

    

    
}
