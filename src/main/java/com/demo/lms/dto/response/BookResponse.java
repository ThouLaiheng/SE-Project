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
    private String categoryName;

    public static BookResponse from(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .categoryName(
                        book.getCategory() != null
                                ? book.getCategory().getName()
                                : null
                )
                .build();
    }
}
