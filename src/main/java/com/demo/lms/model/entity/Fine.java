package com.demo.lms.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "fines")
@Getter
@Setter
public class Fine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "borrow_record_id", unique = true)
    private BorrowRecord borrowRecord;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private boolean paid = false;
}
