package ru.practicum.sht.request.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

import java.util.Map;
import java.util.UUID;

@Value
@Builder
public class ProductReturnRequest {
    UUID orderId;

    @NotNull(message = "Перечень возвращаемых товаров не должен быть null")
    @NotEmpty(message = "Перечень возвращаемых товаров не должен быть пустым")
    Map<@NotNull UUID, @NotNull @Positive Long> products;
}