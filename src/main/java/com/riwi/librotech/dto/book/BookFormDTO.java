package com.riwi.librotech.dto.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookFormDTO {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer yearPublication;
    private Long categoryId;
    private Long publisherId;
    private List<Long> genreIds;
}