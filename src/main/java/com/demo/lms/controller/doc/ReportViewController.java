package com.demo.lms.controller.doc;

import com.demo.lms.model.enums.UserRoleType;
import com.demo.lms.repository.BookRepository;
import com.demo.lms.repository.UserRepository;
import com.demo.lms.repository.BookCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller for handling report and statistics views
 */
@Controller
@RequestMapping("/createReport")
@RequiredArgsConstructor
@Slf4j
public class ReportViewController {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final BookCategoryRepository bookCategoryRepository;

    /**
     * Display the report and statistics page
     */
    @GetMapping
    public String showReportPage(Model model) {
        log.info("Displaying report and statistics page");
        
        // Get basic statistics
        long totalBooks = bookRepository.count();
        long totalUsers = userRepository.count();
        long totalCategories = bookCategoryRepository.count();
        
        // Get books by category statistics
        Map<String, Long> booksByCategory = new HashMap<>();
        bookCategoryRepository.findAll().forEach(category -> {
            long count = bookRepository.countByCategoryId(category.getId());
            booksByCategory.put(category.getName(), count);
        });
        
        // Get user role statistics
        long adminCount = userRepository.countByRole(UserRoleType.ADMIN);
        long studentCount = userRepository.countByRole(UserRoleType.STUDENT);
        long librarianCount = userRepository.countByRole(UserRoleType.LIBRARIAN);
        
        // Add statistics to model
        model.addAttribute("totalBooks", totalBooks);
        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalCategories", totalCategories);
        model.addAttribute("booksByCategory", booksByCategory);
        model.addAttribute("adminCount", adminCount);
        model.addAttribute("studentCount", studentCount);
        model.addAttribute("librarianCount", librarianCount);
        
        return "createReport";
    }
}
