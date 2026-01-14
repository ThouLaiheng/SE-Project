package com.demo.lms.controller.product;

import com.demo.lms.model.entity.Book;
import com.demo.lms.model.entity.BookCategory;
import com.demo.lms.repository.BookCategoryRepository;
import com.demo.lms.repository.BookRepository;
import com.demo.lms.repository.BookCopyRepository;
import com.demo.lms.repository.BorrowRecordRepository;
import com.demo.lms.repository.ReservationRepository;
import com.demo.lms.service.book.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
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
    private final BookCopyRepository bookCopyRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final ReservationRepository reservationRepository;
    private final BookService bookService;

    private static final String UPLOAD_DIR = "uploads"; // base folder for uploaded files

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
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "imageUrl", required = false) String imageUrl,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
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
                model.addAttribute("description", description);
                model.addAttribute("imageUrl", imageUrl);
                model.addAttribute("categoryId", categoryId);
                model.addAttribute("categories", bookCategoryRepository.findAll());
                model.addAttribute("books", bookRepository.findAll());
                return "createProduct";
            }
            
            if (author == null || author.trim().isEmpty()) {
                model.addAttribute("error", "Author is required");
                model.addAttribute("title", title);
                model.addAttribute("author", author);
                model.addAttribute("isbn", isbn);
                model.addAttribute("description", description);
                model.addAttribute("imageUrl", imageUrl);
                model.addAttribute("categoryId", categoryId);
                model.addAttribute("categories", bookCategoryRepository.findAll());
                model.addAttribute("books", bookRepository.findAll());
                return "createProduct";
            }
            
            if (isbn == null || isbn.trim().isEmpty()) {
                model.addAttribute("error", "ISBN is required");
                model.addAttribute("title", title);
                model.addAttribute("author", author);
                model.addAttribute("isbn", isbn);
                model.addAttribute("description", description);
                model.addAttribute("imageUrl", imageUrl);
                model.addAttribute("categoryId", categoryId);
                model.addAttribute("categories", bookCategoryRepository.findAll());
                model.addAttribute("books", bookRepository.findAll());
                return "createProduct";
            }
            
            // Check if ISBN already exists
            if (bookRepository.existsByIsbn(isbn)) {
                model.addAttribute("error", "A book with this ISBN already exists");
                model.addAttribute("title", title);
                model.addAttribute("author", author);
                model.addAttribute("isbn", isbn);
                model.addAttribute("description", description);
                model.addAttribute("imageUrl", imageUrl);
                model.addAttribute("categoryId", categoryId);
                model.addAttribute("categories", bookCategoryRepository.findAll());
                model.addAttribute("books", bookRepository.findAll());
                return "createProduct";
            }
            
            // Handle optional image upload
            String finalImageUrl = imageUrl;
            log.info("Image upload check - imageFile: {}, isEmpty: {}, imageUrl: {}", 
                imageFile != null ? imageFile.getOriginalFilename() : "null", 
                imageFile != null ? imageFile.isEmpty() : "null",
                imageUrl);
            
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    // Ensure we use an absolute, normalized path so Tomcat doesn't resolve it under its temp directory
                    Path dir = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
                    log.info("Upload directory path (absolute): {}", dir);
                    if (!Files.exists(dir)) {
                        Files.createDirectories(dir);
                        log.info("Created upload directory: {}", dir);
                    }
                    String original = imageFile.getOriginalFilename();
                    String safeName = (original == null ? "image" : original).replaceAll("[^a-zA-Z0-9._-]", "_");
                    String filename = UUID.randomUUID() + "_" + safeName;
                    Path target = dir.resolve(filename).toAbsolutePath().normalize();
                    log.info("Saving file to: {}", target);
                    imageFile.transferTo(target.toFile());
                    finalImageUrl = "/" + UPLOAD_DIR + "/" + filename;
                    log.info("File saved successfully. URL: {}", finalImageUrl);
                } catch (IOException ioe) {
                    log.error("Failed to store uploaded image", ioe);
                }
            } else {
                log.info("No image file uploaded, using existing URL: {}", finalImageUrl);
            }

            // Create new book
            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setIsbn(isbn);
            book.setDescription(description);
            book.setImageUrl(finalImageUrl);
            log.info("Setting imageUrl for new book: {}", finalImageUrl);
            
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
            // Ensure at least one available copy exists for borrowing
            try {
                com.demo.lms.model.entity.BookCopy copy = new com.demo.lms.model.entity.BookCopy();
                copy.setBook(savedBook);
                copy.setCopyCode("COPY-" + UUID.randomUUID());
                copy.setAvailable(true);
                bookCopyRepository.save(copy);
                log.info("Created initial available copy for book ID: {}", savedBook.getId());
            } catch (Exception ce) {
                log.warn("Failed to create initial book copy: {}", ce.getMessage());
            }
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
            model.addAttribute("description", description);
            model.addAttribute("imageUrl", imageUrl);
            model.addAttribute("categoryId", categoryId);
            model.addAttribute("categories", bookCategoryRepository.findAll());
            model.addAttribute("books", bookRepository.findAll());
            return "createProduct";
        }
    }

    /**
     * Handle book update request
     */
    @PostMapping("/{id}/update")
        public String updateProduct(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("isbn") String isbn,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "imageUrl", required = false) String imageUrl,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            RedirectAttributes redirectAttributes) {
        
        try {
            log.info("Processing product update request for ID: {}", id);
            
            Book book = bookRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Book not found with ID: " + id));
            
            // Check if ISBN is changed and already exists
            if (!book.getIsbn().equals(isbn) && bookRepository.existsByIsbn(isbn)) {
                redirectAttributes.addFlashAttribute("error", "A book with this ISBN already exists");
                return "redirect:/createProduct";
            }
            
            // Handle optional new image upload; fall back to provided URL or keep existing
            String finalImageUrl = imageUrl;
            if (imageFile != null && !imageFile.isEmpty()) {
                try {
                    Path dir = Paths.get(UPLOAD_DIR).toAbsolutePath().normalize();
                    if (!Files.exists(dir)) {
                        Files.createDirectories(dir);
                    }
                    String original = imageFile.getOriginalFilename();
                    String safeName = (original == null ? "image" : original).replaceAll("[^a-zA-Z0-9._-]", "_");
                    String filename = UUID.randomUUID() + "_" + safeName;
                    Path target = dir.resolve(filename).toAbsolutePath().normalize();
                    imageFile.transferTo(target.toFile());
                    finalImageUrl = "/" + UPLOAD_DIR + "/" + filename;
                } catch (IOException ioe) {
                    log.warn("Failed to store uploaded image: {}", ioe.getMessage());
                }
            }

            // Update book fields
            book.setTitle(title);
            book.setAuthor(author);
            book.setIsbn(isbn);
            book.setDescription(description);
            book.setImageUrl(finalImageUrl);
            
            // Set category if provided
            if (categoryId != null) {
                BookCategory category = bookCategoryRepository.findById(categoryId)
                        .orElse(null);
                book.setCategory(category);
            } else {
                book.setCategory(null);
            }
            
            // Save updated book
            bookRepository.save(book);
            log.info("Book updated successfully with ID: {}", id);
            
            redirectAttributes.addFlashAttribute("success", "Book updated successfully!");
            return "redirect:/createProduct";
            
        } catch (Exception e) {
            log.error("Error updating book with ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "Failed to update book: " + e.getMessage());
            return "redirect:/createProduct";
        }
    }

    /**
     * Handle book deletion request
     */
    @PostMapping("/{id}/delete")
    public String deleteProduct(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        
        try {
            log.info("Processing product deletion request for ID: {}", id);
            
            if (!bookRepository.existsById(id)) {
                redirectAttributes.addFlashAttribute("error", "Book not found with ID: " + id);
                return "redirect:/createProduct";
            }
            
            // Use BookService to properly handle cascade deletion
            bookService.delete(id);
            log.info("Book deleted successfully with ID: {}", id);
            
            redirectAttributes.addFlashAttribute("success", "Book deleted successfully!");
            return "redirect:/createProduct";
            
        } catch (Exception e) {
            log.error("Error deleting book with ID: {}", id, e);
            redirectAttributes.addFlashAttribute("error", "Failed to delete book: " + e.getMessage());
            return "redirect:/createProduct";
        }
    }
    
    /**
     * Clear all data (reservations, borrow records, book copies)
     * This allows books to be deleted without foreign key constraint violations
     */
    @PostMapping("/cleanup-all")
    @ResponseBody
    public String clearAllData() {
        try {
            log.warn("⚠️ CLEARING ALL DATA - Reservations, Borrow Records, and Book Copies");
            
            long reservationsCount = reservationRepository.count();
            long borrowRecordsCount = borrowRecordRepository.count();
            long bookCopiesCount = bookCopyRepository.count();
            
            reservationRepository.deleteAll();
            borrowRecordRepository.deleteAll();
            bookCopyRepository.deleteAll();
            
            String message = String.format(
                "✅ Successfully deleted: %d reservations, %d borrow records, %d book copies",
                reservationsCount, borrowRecordsCount, bookCopiesCount
            );
            
            log.info(message);
            return message;
            
        } catch (Exception e) {
            log.error("❌ Error clearing data: {}", e.getMessage(), e);
            return "❌ Error: " + e.getMessage();
        }
    }
}
