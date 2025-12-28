package com.demo.lms.controller.notification;

import com.demo.lms.model.entity.Notification;
import com.demo.lms.service.notification.NotificationService;
import com.demo.lms.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * Get my notifications
     */
    @GetMapping
    public ResponseEntity<List<Notification>> getMyNotifications() {
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(notificationService.getMyNotifications(userId));
    }

    /**
     * Mark notification as read
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        notificationService.markAsRead(id, userId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Count unread notifications
     */
    @GetMapping("/unread/count")
    public ResponseEntity<Long> countUnread() {
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(notificationService.countUnread(userId));
    }
}
