package ru.practicum.sht.dto.delivery;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import ru.practicum.sht.dto.warehouse.AddressDto;

import java.util.UUID;

@Value
@Builder
public class DeliveryDto {

    @NotNull(message = "ID доставки не должен быть null")
    UUID deliveryId;

    @NotNull(message = "ID доставки не должен быть null")
    AddressDto fromAddress;

    @NotNull(message = "ID доставки не должен быть null")
    AddressDto toAddress;

    @NotNull(message = "ID заказа не должен быть null")
    UUID orderId;

    @NotNull(message = "Статус доставки не должен быть null")
    DeliveryState deliveryState;
}