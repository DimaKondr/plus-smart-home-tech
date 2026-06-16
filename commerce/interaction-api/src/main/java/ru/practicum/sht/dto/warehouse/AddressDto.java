package ru.practicum.sht.dto.warehouse;

import lombok.*;

@Value
@Builder
//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode
//@AllArgsConstructor
public class AddressDto {
    String country;
    String city;
    String street;
    String house;
    String flat;
}