package ru.practicum.sht.request.shopping.cart;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ChangeProductQuantityRequest {

    @NotNull(message = "ID товара не должно быть null")
    UUID productId;

    @NotNull(message = "Измененное количество товара не должно быть null")
    Long newQuantity;
}