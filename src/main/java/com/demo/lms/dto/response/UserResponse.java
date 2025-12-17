package com.demo.lms.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private Set<String> roles;
}
