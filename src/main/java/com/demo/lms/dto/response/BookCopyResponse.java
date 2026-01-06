package com.demo.lms.dto.response;

import com.demo.lms.model.entity.BookCopy;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookCopyResponse {

    private Long id;
    private String copyCode;
    private boolean available;
    private Long bookId;
    private String bookTitle;

    public static BookCopyResponse from(BookCopy copy) {
        return BookCopyResponse.builder()
                .id(copy.getId())
                .copyCode(copy.getCopyCode())
                .available(copy.isAvailable())
                .bookId(copy.getBook().getId())
                .bookTitle(copy.getBook().getTitle())
                .build();
    }
}
