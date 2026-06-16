package ru.practicum.sht.request.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Value
@Builder
//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode
//@AllArgsConstructor
public class AddProductToWarehouseRequest {

    @NotNull(message = "ID товара не должно быть null")
    //String productId;
    UUID productId;

    @NotNull(message = "Количество товара не может быть null")
    @Min(value = 1, message = "Количество товара должно быть не менее 1")
    Long quantity;
}