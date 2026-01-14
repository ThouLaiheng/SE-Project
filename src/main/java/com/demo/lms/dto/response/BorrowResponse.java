package com.demo.lms.dto.response;

import com.demo.lms.model.entity.BorrowRecord;
import com.demo.lms.model.enums.BorrowStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BorrowResponse {

    private Long id;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long bookCopyId;
    private String bookTitle;
    private String bookAuthor;
    private String bookImageUrl;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private BorrowStatus status;

    public static BorrowResponse from(BorrowRecord record) {
        String bookTitle = null;
        String bookAuthor = null;
        String bookImageUrl = null;
        
        if (record.getBookCopy() != null) {
            if (record.getBookCopy().getBook() != null) {
                bookTitle = record.getBookCopy().getBook().getTitle();
                bookAuthor = record.getBookCopy().getBook().getAuthor();
                bookImageUrl = record.getBookCopy().getBook().getImageUrl();
            }
        }
        
        return BorrowResponse.builder()
                .id(record.getId())
                .userId(record.getUser() != null ? record.getUser().getId() : null)
                .userName(record.getUser() != null ? record.getUser().getName() : null)
                .userEmail(record.getUser() != null ? record.getUser().getEmail() : null)
                .bookCopyId(record.getBookCopy() != null ? record.getBookCopy().getId() : null)
                .bookTitle(bookTitle)
                .bookAuthor(bookAuthor)
                .bookImageUrl(bookImageUrl)
                .borrowDate(record.getBorrowDate())
                .dueDate(record.getDueDate())
                .returnDate(record.getReturnDate())
                .status(record.getStatus())
                .build();
    }
}
