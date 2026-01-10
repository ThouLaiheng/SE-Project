package com.demo.lms.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCopyRequest {

    private Long bookId;
    private String copyCode;
}
