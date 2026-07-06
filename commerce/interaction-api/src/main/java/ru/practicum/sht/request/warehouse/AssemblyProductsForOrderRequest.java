package ru.practicum.sht.request.warehouse;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

import java.util.Map;
import java.util.UUID;

@Value
@Builder
public class AssemblyProductsForOrderRequest {

    @NotNull(message = "ID заказа для сборки не должен быть null")
    UUID orderId;

    @NotNull(message = "Перечень товаров для сборки не должен быть null")
    @NotEmpty(message = "Перечень товаров для сборки не должен быть пустой")
    Map<@NotNull UUID, @NotNull @Positive Long> products;
}