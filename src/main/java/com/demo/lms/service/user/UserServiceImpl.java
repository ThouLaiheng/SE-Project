package com.demo.lms.service.user;

import com.demo.lms.dto.request.CreateUserRequest;
import com.demo.lms.dto.request.UpdateUserRequest;
import com.demo.lms.dto.response.UserResponse;
import com.demo.lms.exception.UserAlreadyExistsException;
import com.demo.lms.exception.UserNotFoundException;
import com.demo.lms.mapper.UserMapper;
import com.demo.lms.model.entity.Role;
import com.demo.lms.model.entity.User;
import com.demo.lms.model.entity.UserRole;
import com.demo.lms.model.enums.UserRoleType;
import com.demo.lms.repository.RoleRepository;
import com.demo.lms.repository.UserRepository;
import com.demo.lms.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse createUser(CreateUserRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);

        Role role = roleRepository.findByName(UserRoleType.STUDENT)
                .orElseThrow(() -> new RuntimeException("ROLE STUDENT not found"));

        UserRole userRole = new UserRole();
        userRole.setUser(savedUser);
        userRole.setRole(role);

        userRoleRepository.save(userRole);

        savedUser.setUserRoles(Set.of(userRole));

        return UserMapper.toResponse(savedUser);
    }


    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        return userRepository.findByIdWithRoles(id)
                .map(UserMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAllWithRoles()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        log.info("Updating user with ID: {}", id);

        User user = userRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        // Check if email is being changed and if it already exists
        if (!user.getEmail().equals(request.getEmail())) {
            Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
            if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
                throw new UserAlreadyExistsException("Email already exists");
            }
        }

        // Update user fields
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // Update password only if provided
        if (StringUtils.hasText(request.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Update enabled status if provided
        if (request.getEnabled() != null) {
            user.setEnabled(request.getEnabled());
        }

        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with ID: {}", id);

        return UserMapper.toResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("Deleting user with ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        // Delete user roles first (due to foreign key constraints)
        userRoleRepository.deleteByUserId(id);

        // Delete the user
        userRepository.delete(user);

        log.info("User deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional
    public UserResponse updateUserStatus(Long id, boolean enabled) {
        log.info("Updating user status for ID: {} to enabled: {}", id, enabled);

        User user = userRepository.findByIdWithRoles(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        user.setEnabled(enabled);
        User updatedUser = userRepository.save(user);

        log.info("User status updated successfully for ID: {}", id);

        return UserMapper.toResponse(updatedUser);
    }

}
