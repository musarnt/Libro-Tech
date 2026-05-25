package com.riwi.librotech.dto.book;
import lombok.Data;
import java.util.List;
@Data
public class BookResponseDTO {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private Integer yearPublication;
    private String categoryName;
    private String publisherName;
    private List<String> genreNames;
}
