package com.demo.lms.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 255)
    private String message;

    /**
     * IMPORTANT:
     * Java field name = readFlag
     * DB column name = read_flag
     */
    @Column(name = "read_flag")
    private boolean readFlag = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
