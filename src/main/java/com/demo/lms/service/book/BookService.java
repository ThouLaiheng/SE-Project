package com.demo.lms.service.book;

import com.demo.lms.dto.request.BookRequest;
import com.demo.lms.model.entity.Book;
import com.demo.lms.model.entity.BookCategory;
import com.demo.lms.repository.BookCategoryRepository;
import com.demo.lms.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final BookCategoryRepository categoryRepository;

    public Book create(BookRequest request) {
        Book book = new Book();
        mapRequest(book, request);
        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));
    }

    public Book update(Long id, BookRequest request) {
        Book book = findById(id);
        mapRequest(book, request);
        return bookRepository.save(book);
    }

    public void delete(Long id) {
        Book book = findById(id);
        bookRepository.delete(book);
    }

    private void mapRequest(Book book, BookRequest request) {
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setIsbn(request.getIsbn());

        if (request.getCategoryId() != null) {
            BookCategory category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            book.setCategory(category);
        }
    }
}
