package com.demo.lms.dto.response;

import java.util.List;

public class AuthResponse {
    private String email;
    private String token;
    private List<String> roles;

    public String getEmail() {
        return email;
    }

    public AuthResponse(String email, String token, List<String> roles) {
        this.email = email;
        this.token = token;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
    
   