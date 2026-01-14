package com.demo.lms.dto.response;

import com.demo.lms.model.entity.Book;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private String isbn;
    // Keep a generic field name for the UI
    private String category; 
    private String description;
    private String imageUrl;
    private int availableCopies;
    private int totalCopies;

    public static BookResponse from(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .category(book.getCategory() != null ? book.getCategory().getName() : null)
                .description(book.getDescription())
                .imageUrl(book.getImageUrl())
                .build();
    }

    public static BookResponse fromWithCounts(Book book, int totalCopies, int availableCopies) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .category(book.getCategory() != null ? book.getCategory().getName() : null)
                .description(book.getDescription())
                .imageUrl(book.getImageUrl())
                .totalCopies(totalCopies)
                .availableCopies(availableCopies)
                .build();
    }
}
