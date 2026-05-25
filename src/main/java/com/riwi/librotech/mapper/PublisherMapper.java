package com.riwi.librotech.mapper;

import com.riwi.librotech.dto.publisher.PublisherRequestDTO;
import com.riwi.librotech.dto.publisher.PublisherResponseDTO;
import com.riwi.librotech.model.Publisher;
import org.springframework.stereotype.Component;

@Component
public class PublisherMapper {

    public PublisherResponseDTO toResponse(Publisher publisher) {
        PublisherResponseDTO dto = new PublisherResponseDTO();
        dto.setId(publisher.getId());
        dto.setName(publisher.getName());
        dto.setAddress(publisher.getAddress());
        dto.setCountry(publisher.getCountry());
        dto.setFoundedIn(publisher.getFoundedIn());
        return dto;
    }

    public Publisher toEntity(PublisherRequestDTO dto) {
        Publisher publisher = new Publisher();
        publisher.setName(dto.getName());
        publisher.setAddress(dto.getAddress());
        publisher.setCountry(dto.getCountry());
        publisher.setFoundedIn(dto.getFoundedIn());
        return publisher;
    }
}
