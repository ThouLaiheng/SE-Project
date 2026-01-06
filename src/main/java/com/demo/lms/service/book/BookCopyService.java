package com.demo.lms.service.book;

import com.demo.lms.dto.request.BookCopyRequest;
import com.demo.lms.model.entity.Book;
import com.demo.lms.model.entity.BookCopy;
import com.demo.lms.repository.BookCopyRepository;
import com.demo.lms.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookCopyService {

    private final BookCopyRepository copyRepository;
    private final BookRepository bookRepository;

    /**
     * Create a new book copy
     */
    public BookCopy create(BookCopyRequest request) {

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        BookCopy copy = new BookCopy();
        copy.setBook(book);
        copy.setCopyCode(request.getCopyCode());
        copy.setAvailable(true);

        return copyRepository.save(copy);
    }

    /**
     * Get all copies of a book
     */
    @Transactional(readOnly = true)
    public List<BookCopy> findByBook(Long bookId) {
        return copyRepository.findByBookId(bookId);
    }

    /**
     * Update availability (ADMIN)
     */
    public BookCopy updateAvailability(Long copyId, boolean available) {

        BookCopy copy = copyRepository.findById(copyId)
                .orElseThrow(() -> new EntityNotFoundException("Book copy not found"));

        copy.setAvailable(available);
        return copyRepository.save(copy);
    }

    /**
     * Delete copy
     */
    public void delete(Long id) {
        BookCopy copy = copyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book copy not found"));

        copyRepository.delete(copy);
    }
}
