package ru.practicum.sht.request.warehouse;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import ru.practicum.sht.dto.warehouse.DimensionDto;

import java.util.UUID;

@Value
@Builder
public class NewProductInWarehouseRequest {

    @NotNull(message = "ID товара не должен быть null")
    UUID productId;
    Boolean fragile;

    @NotNull(message = "Данные о размерах товара не должны быть null")
    @Valid
    DimensionDto dimension;

    @NotNull(message = "Вес товара не может быть null")
    @Min(value = 1, message = "Вес товара должен быть не менее 1")
    Double weight;
}