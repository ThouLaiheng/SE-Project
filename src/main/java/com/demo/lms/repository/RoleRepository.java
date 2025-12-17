package com.demo.lms.repository;

import com.demo.lms.model.entity.Role;
import com.demo.lms.model.enums.UserRoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRoleType name);
}
