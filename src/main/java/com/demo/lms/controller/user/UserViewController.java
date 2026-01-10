package com.demo.lms.controller.user;

import com.demo.lms.model.entity.User;
import com.demo.lms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for handling Thymeleaf UI views for user management
 */
@Controller
@RequestMapping("/createUser")
@RequiredArgsConstructor
@Slf4j
public class UserViewController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Display the user management page
     */
    @GetMapping
    public String showUserManagementPage(Model model) {
        log.info("Displaying user management page");
        
        // Fetch all users for the table
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        
        return "createUser";
    }

    /**
     * Handle form submission for creating a new user
     */
    @PostMapping
    public String createUser(
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        try {
            log.info("Processing user creation request for email: {}", email);
            
            // Validate inputs
            if (name == null || name.trim().isEmpty()) {
                model.addAttribute("error", "Name is required");
                model.addAttribute("users", userRepository.findAll());
                return "createUser";
            }
            
            if (email == null || email.trim().isEmpty()) {
                model.addAttribute("error", "Email is required");
                model.addAttribute("users", userRepository.findAll());
                return "createUser";
            }
            
            if (password == null || password.trim().isEmpty()) {
                model.addAttribute("error", "Password is required");
                model.addAttribute("users", userRepository.findAll());
                return "createUser";
            }
            
            if (password.length() < 6) {
                model.addAttribute("error", "Password must be at least 6 characters");
                model.addAttribute("users", userRepository.findAll());
                return "createUser";
            }
            
            // Check if email already exists
            if (userRepository.existsByEmail(email)) {
                model.addAttribute("error", "A user with this email already exists");
                model.addAttribute("users", userRepository.findAll());
                return "createUser";
            }
            
            // Create new user
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setEnabled(true);
            user.setRole("USER");
            
            // Save user
            User savedUser = userRepository.save(user);
            log.info("User created successfully with ID: {}", savedUser.getId());
            
            // Add success message
            redirectAttributes.addFlashAttribute("success", 
                "User created successfully! ID: " + savedUser.getId());
            
            return "redirect:/createUser";
            
        } catch (Exception e) {
            log.error("Error creating user with email: {}", email, e);
            model.addAttribute("error", "Failed to create user: " + e.getMessage());
            model.addAttribute("users", userRepository.findAll());
            return "createUser";
        }
    }

    /**
     * Handle user deletion by ID
     */
    @PostMapping("/deleteUser/{id}")
    public String deleteUser(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            log.info("Processing user deletion request for ID: {}", id);
            
            // Check if user exists
            if (!userRepository.existsById(id)) {
                log.warn("User with ID {} not found", id);
                redirectAttributes.addFlashAttribute("error", 
                    "User not found with ID: " + id);
                return "redirect:/createUser";
            }
            
            // Get user name before deletion for logging
            User user = userRepository.findById(id).orElse(null);
            String userName = user != null ? user.getName() : "Unknown";
            
            // Delete the user
            userRepository.deleteById(id);
            log.info("User deleted successfully - ID: {}, Name: {}", id, userName);
            
            redirectAttributes.addFlashAttribute("success", 
                "User '" + userName + "' deleted successfully!");
            
            return "redirect:/createUser";
            
        } catch (Exception e) {
            log.error("Error deleting user with ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error", 
                "Failed to delete user: " + e.getMessage());
            return "redirect:/createUser";
        }
    }
}
