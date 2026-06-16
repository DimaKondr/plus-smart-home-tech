package ru.practicum.sht.dto.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Value
@Builder
//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode
//@AllArgsConstructor
public class BookedProductsDto {

    @NotNull(message = "Общий вес товаров не должен быть null")
    Double deliveryWeight;

    @NotNull(message = "Общий объем товаров не должен быть null")
    Double deliveryVolume;

    @NotNull(message = "Данные о наличии хрупких товаров не должны быть null")
    Boolean fragile;
}