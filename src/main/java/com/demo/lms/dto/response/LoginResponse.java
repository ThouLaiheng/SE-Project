package com.demo.lms.dto.response;

public class LoginResponse {
    private String email;
    private String encodedPassword;

    public LoginResponse(String email, String encodedPassword) {
        this.email = email;
        this.encodedPassword = encodedPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEncodedPassword() {
        return encodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        this.encodedPassword = encodedPassword;
    }
}
