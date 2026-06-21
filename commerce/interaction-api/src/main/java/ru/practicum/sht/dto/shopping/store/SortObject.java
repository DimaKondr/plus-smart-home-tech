package ru.practicum.sht.dto.shopping.store;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SortObject {
    String direction;
    String nullHandling;
    Boolean ascending;
    String property;
    Boolean ignoreCase;
}