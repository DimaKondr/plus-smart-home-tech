package ru.practicum.sht.dto.warehouse;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AddressDto {
    String country;
    String city;
    String street;
    String house;
    String flat;
}