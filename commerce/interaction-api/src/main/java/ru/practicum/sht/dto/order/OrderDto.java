package ru.practicum.sht.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Value
@Builder
public class OrderDto {

    @NotNull
    UUID orderId;
    UUID shoppingCartId;

    @NotNull(message = "Перечень товаров в заказе не должен быть null")
    @NotEmpty(message = "Перечень товаров в заказе не должен быть пустым")
    Map<@NotNull UUID, @NotNull @Positive Long> products;
    UUID paymentId;
    UUID deliveryId;
    OrderState state;
    Double deliveryWeight;
    Double deliveryVolume;
    Boolean fragile;
    BigDecimal totalPrice;
    BigDecimal deliveryPrice;
    BigDecimal productPrice;
}