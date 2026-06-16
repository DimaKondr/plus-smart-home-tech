package ru.practicum.sht.dto.shopping.store;

import lombok.*;

@Value
@Builder
//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode
//@AllArgsConstructor
public class SortObject {
    String direction;
    String nullHandling;
    Boolean ascending;
    String property;
    Boolean ignoreCase;
}