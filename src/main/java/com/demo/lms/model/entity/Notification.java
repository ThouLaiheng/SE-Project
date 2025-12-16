package com.demo.lms.model.entity;

import com.demo.lms.base.BaseEntity;
import com.demo.lms.model.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private NotificationType type;

    @Column(length = 255)
    private String message;

    @Column(name = "read_flag")
    private boolean readFlag = false;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
}
