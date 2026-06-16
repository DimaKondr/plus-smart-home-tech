package ru.practicum.sht.dto.shopping.cart;

import jakarta.validation.constraints.NotEmpty;import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;import lombok.*;

import java.util.Map;import java.util.UUID;

@Value
@Builder
//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode
//@AllArgsConstructor
public class ShoppingCartDto {

    @NotNull(message = "ID корзины не должно быть null")
    //String shoppingCartId;
    UUID shoppingCartId;

    @NotNull(message = "Корзина товаров не должна быть null")
    @NotEmpty(message = "Корзина товаров не должна быть пустой")
    //Map<String, Long> products;
    Map<@NotNull UUID, @NotNull @Positive Long> products;
}