package com.demo.lms.config;

import com.demo.lms.model.entity.Role;
import com.demo.lms.model.enums.UserRoleType;
import com.demo.lms.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Database initialization component that runs after application startup.
 * Ensures that default roles exist in the database.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting database initialization...");
        initializeRoles();
        log.info("Database initialization completed successfully!");
    }

    /**
     * Initialize default roles in the database if they don't exist.
     */
    private void initializeRoles() {
        log.info("Checking and initializing default roles...");

        for (UserRoleType roleType : UserRoleType.values()) {
            if (roleRepository.findByName(roleType).isEmpty()) {
                Role role = new Role();
                role.setName(roleType);
                roleRepository.save(role);
                log.info("Created role: {}", roleType);
            } else {
                log.debug("Role {} already exists", roleType);
            }
        }

        log.info("Role initialization completed. Available roles: {}", (Object[]) UserRoleType.values());
    }
}
