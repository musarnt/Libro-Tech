package com.riwi.librotech.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // H2 maneja el autoincremento
    private Long id;

    private String title;
    private String author;
    private String isbn;
    private Integer yearPublication;
}