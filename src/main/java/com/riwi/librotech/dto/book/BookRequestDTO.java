package com.riwi.librotech.dto.book;
import com.riwi.librotech.validation.PastOrPresentYear;
import com.riwi.librotech.validation.ValidISBN;
import jakarta.validation.constraints.*;

import java.util.List;

public record   BookRequestDTO(
        @NotBlank(message = "Title is required")
        @Size(min = 2, max = 255, message = "Title must be between 2 and 255 characters")
        String title,

        @NotBlank(message = "Author is required")
        String author,

        @NotBlank(message = "ISBN is required")
        @ValidISBN
        String isbn,

        @NotNull(message = "Publication year is required")
        @Min(value = 1450, message = "Publication year must be 1450 or later")
        @PastOrPresentYear
        Integer yearPublication,

        @NotNull(message = "Category ID is required")
        @Positive(message = "Invalid category ID")
        Long categoryId,

        @NotNull(message = "Publisher ID is required")
        @Positive(message = "Invalid publisher ID")
        Long publisherId,

        @NotEmpty(message = "At least one genre is required")
        List<@NotNull @Positive Long> genreIds
) {}
