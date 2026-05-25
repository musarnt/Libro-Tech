package com.riwi.librotech.dto.book;
import lombok.Data;
import java.util.List;
@Data
public class BookRequestDTO {
    private String title;
    private String author;
    private String isbn;
    private Integer yearPublication;
    private Long categoryId;
    private Long publisherId;
    private List<Long> genreIds;
}
