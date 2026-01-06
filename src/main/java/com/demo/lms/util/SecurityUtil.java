package com.demo.lms.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {
    }

    /**
     * Get current authenticated user ID
     * Assumes user ID is stored as principal name (String)
     * or can be parsed safely.
     */
    public static Long getCurrentUserId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("User is not authenticated");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof String principalStr) {
            try {
                return Long.parseLong(principalStr);
            } catch (NumberFormatException ex) {
                throw new IllegalStateException(
                        "Authenticated principal is not a valid user ID"
                );
            }
        }

        try {
            var method = principal.getClass().getMethod("getId");
            Object id = method.invoke(principal);
            if (id instanceof Long longId) {
                return longId;
            }
        } catch (Exception ignored) {
            // Reflection failed, try other approaches
        }

        throw new IllegalStateException("Unable to extract user ID from security context");
    }
}
