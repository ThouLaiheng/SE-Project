package com.demo.lms.dto.request;

import com.demo.lms.model.enums.ContactStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateContactStatusRequest {

    @NotNull(message = "Status is required")
    private ContactStatus status;

    private String adminResponse;
}
