package com.demo.lms.mapper;

import com.demo.lms.dto.response.UserResponse;
import com.demo.lms.model.entity.User;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserMapper {

    private UserMapper() {}

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .roles(
                        Optional.ofNullable(user.getUserRoles())
                                .orElse(Collections.emptySet())
                                .stream()
                                .filter(userRole -> userRole != null
                                        && userRole.getRole() != null
                                        && userRole.getRole().getName() != null)
                                .map(userRole -> userRole.getRole().getName().name())
                                .collect(Collectors.toSet())
                )
                .build();
    }
}
