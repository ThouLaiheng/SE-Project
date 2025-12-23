package com.demo.lms.dto.response;

public class AuthResponse {
    private String email;
    private String token;

    public String getEmail() {
        return email;
    }

    public AuthResponse(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    
}
    
   