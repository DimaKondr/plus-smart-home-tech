package ru.practicum.sht.dto.shopping.store;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PageableObject {
    Long offset;
    SortObject sort;
    Boolean unpaged;
    Boolean paged;
    Integer pageNumber;
    Integer pageSize;
}