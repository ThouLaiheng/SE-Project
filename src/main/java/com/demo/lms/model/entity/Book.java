package com.demo.lms.model.entity;

import com.demo.lms.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter
public class Book extends BaseEntity {

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 150)
    private String author;

    @Column(unique = true, length = 50)
    private String isbn;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private BookCategory category;
}
