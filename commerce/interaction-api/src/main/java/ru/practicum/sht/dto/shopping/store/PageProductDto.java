package ru.practicum.sht.dto.shopping.store;

import jakarta.validation.Valid;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class PageProductDto {
    Long totalElements;
    Integer totalPages;
    Boolean first;
    Boolean last;
    Integer size;

    @Valid
    List<ProductDto> content;
    Integer number;
    List<SortObject> sort;
    Integer numberOfElements;
    PageableObject pageable;
    Boolean empty;
}