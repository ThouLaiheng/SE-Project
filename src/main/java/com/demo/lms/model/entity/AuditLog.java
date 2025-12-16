package com.demo.lms.model.entity;

import com.demo.lms.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class AuditLog extends BaseEntity {

    @Column(length = 255)
    private String action;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
