package com.riwi.librotech.dto.publisher;
import lombok.Data;
@Data
public class PublisherResponseDTO {
    private Long id;
    private String name;
    private String address;
    private String country;
    private Integer foundedIn;
}
