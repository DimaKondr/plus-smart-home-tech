package ru.practicum.sht.dto.shopping.store;

import lombok.*;

@Value
@Builder
//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode
//@AllArgsConstructor
public class PageableObject {
    Long offset;
    SortObject sort;
    Boolean unpaged;
    Boolean paged;
    Integer pageNumber;
    Integer pageSize;
}