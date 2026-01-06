package com.demo.lms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateContactRequest {

    @NotBlank
    private String subject;

    @NotBlank
    private String message;
}
