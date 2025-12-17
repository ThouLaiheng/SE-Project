package com.demo.lms.service.user;

import com.demo.lms.dto.request.CreateUserRequest;
import com.demo.lms.dto.request.UpdateUserRequest;
import com.demo.lms.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(CreateUserRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse updateUser(Long id, UpdateUserRequest request);

    void deleteUser(Long id);

    UserResponse updateUserStatus(Long id, boolean enabled);
}
