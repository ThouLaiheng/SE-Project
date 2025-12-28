package com.demo.lms.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookRequest {

    private String title;
    private String author;
    private String isbn;
    private Long categoryId;
}
