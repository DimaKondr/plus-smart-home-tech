package ru.practicum.sht.dto.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Value
@Builder
//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode
//@AllArgsConstructor
public class DimensionDto {

    @NotNull(message = "Ширина товара не может быть null")
    @Min(value = 1, message = "Ширина товара должна быть не менее 1")
    Double width;

    @NotNull(message = "Высота товара не может быть null")
    @Min(value = 1, message = "Высота товара должна быть не менее 1")
    Double height;

    @NotNull(message = "Глубина товара не может быть null")
    @Min(value = 1, message = "Глубина товара должна быть не менее 1")
    Double depth;
}