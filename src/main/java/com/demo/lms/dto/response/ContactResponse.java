package com.demo.lms.dto.response;

import com.demo.lms.model.enums.ContactStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ContactResponse {

    private Long id;
    private String subject;
    private String message;
    private ContactStatus status;
    private String adminResponse;

    private String userName;
    private String userEmail;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime respondedAt;
}
