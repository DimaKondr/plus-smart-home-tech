package ru.practicum.sht.request.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import ru.practicum.sht.dto.shopping.cart.ShoppingCartDto;
import ru.practicum.sht.dto.warehouse.AddressDto;

@Value
@Builder
public class CreateNewOrderRequest {

    @NotNull(message = "Корзина товаров не должна быть null")
    @Valid
    ShoppingCartDto shoppingCart;

    @NotNull(message = "Адрес доставки не должен быть null")
    @Valid
    AddressDto deliveryAddress;
}