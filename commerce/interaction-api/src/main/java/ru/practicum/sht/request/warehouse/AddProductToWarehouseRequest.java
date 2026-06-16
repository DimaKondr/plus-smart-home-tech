package ru.practicum.sht.request.warehouse;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class AddProductToWarehouseRequest {

    @NotNull(message = "ID товара не должно быть null")
    UUID productId;

    @NotNull(message = "Количество товара не может быть null")
    @Min(value = 1, message = "Количество товара должно быть не менее 1")
    Long quantity;
}