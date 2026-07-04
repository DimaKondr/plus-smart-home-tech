package ru.practicum.sht.request.warehouse;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ShippedToDeliveryRequest {

    @NotNull(message = "ID заказа не должен быть null")
    UUID orderId;

    @NotNull(message = "ID доставки не должен быть null")
    UUID deliveryId;
}