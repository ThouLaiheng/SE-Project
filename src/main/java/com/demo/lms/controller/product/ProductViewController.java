package com.demo.lms.controller.product;

import com.demo.lms.model.entity.Book;
import com.demo.lms.model.entity.BookCategory;
import com.demo.lms.repository.BookCategoryRepository;
import com.demo.lms.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller for handling Thymeleaf UI views for product (book) management
 */
@Controller
@RequestMapping("/createProduct")
@RequiredArgsConstructor
@Slf4j
public class ProductViewController {

    private final BookRepository bookRepository;
    private final BookCategoryRepository bookCategoryRepository;

    /**
     * Display the create product (book) form
     */
    @GetMapping
    public String showCreateProductForm(Model model) {
        log.info("Displaying create product form");
        
        // Fetch all categories for the dropdown
        List<BookCategory> categories = bookCategoryRepository.findAll();
        model.addAttribute("categories", categories);
        
        // Fetch all books for the table
        List<Book> books = bookRepository.findAll();
        model.addAttribute("books", books);
        
        return "createProduct";
    }

    /**
     * Handle form submission for creating a new product (book)
     */
    @PostMapping
    public String createProduct(
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("isbn") String isbn,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        try {
            log.info("Processing product creation request for ISBN: {}", isbn);
            
            // Validate inputs
            if (title == null || title.trim().isEmpty()) {
                model.addAttribute("error", "Title is required");
                model.addAttribute("title", title);
                model.addAttribute("author", author);
                model.addAttribute("isbn", isbn);
                model.addAttribute("categoryId", categoryId);
                model.addAttribute("categories", bookCategoryRepository.findAll());
                return "createProduct";
            }
            
            if (author == null || author.trim().isEmpty()) {
                model.addAttribute("error", "Author is required");
                model.addAttribute("title", title);
                model.addAttribute("author", author);
                model.addAttribute("isbn", isbn);
                model.addAttribute("categoryId", categoryId);
                model.addAttribute("categories", bookCategoryRepository.findAll());
                return "createProduct";
            }
            
            if (isbn == null || isbn.trim().isEmpty()) {
                model.addAttribute("error", "ISBN is required");
                model.addAttribute("title", title);
                model.addAttribute("author", author);
                model.addAttribute("isbn", isbn);
                model.addAttribute("categoryId", categoryId);
                model.addAttribute("categories", bookCategoryRepository.findAll());
                return "createProduct";
            }
            
            // Check if ISBN already exists
            if (bookRepository.existsByIsbn(isbn)) {
                model.addAttribute("error", "A book with this ISBN already exists");
                model.addAttribute("title", title);
                model.addAttribute("author", author);
                model.addAttribute("isbn", isbn);
                model.addAttribute("categoryId", categoryId);
                model.addAttribute("categories", bookCategoryRepository.findAll());
                return "createProduct";
            }
            
            // Create new book
            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setIsbn(isbn);
            
            // Set category if provided
            if (categoryId != null) {
                BookCategory category = bookCategoryRepository.findById(categoryId)
                        .orElse(null);
                if (category != null) {
                    book.setCategory(category);
                }
            }
            
            // Save book
            Book savedBook = bookRepository.save(book);
            log.info("Book created successfully with ID: {}", savedBook.getId());
            
            // Add success message
            redirectAttributes.addFlashAttribute("success", 
                "Book created successfully! ID: " + savedBook.getId());
            
            return "redirect:/createProduct";
            
        } catch (Exception e) {
            log.error("Error creating book with ISBN: {}", isbn, e);
            model.addAttribute("error", "Failed to create book: " + e.getMessage());
            model.addAttribute("title", title);
            model.addAttribute("author", author);
            model.addAttribute("isbn", isbn);
            model.addAttribute("categoryId", categoryId);
            model.addAttribute("categories", bookCategoryRepository.findAll());
            return "createProduct";
        }
    }
}
