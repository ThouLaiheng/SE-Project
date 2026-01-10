package com.demo.lms.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility to generate BCrypt password hashes for migration
 * Run this class to generate password hashes
 */
public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        
        System.out.println("BCrypt Password Hashes:");
        System.out.println("========================");
        System.out.println("admin123: " + encoder.encode("admin123"));
        System.out.println("librarian123: " + encoder.encode("librarian123"));
        System.out.println("student123: " + encoder.encode("student123"));
    }
}
