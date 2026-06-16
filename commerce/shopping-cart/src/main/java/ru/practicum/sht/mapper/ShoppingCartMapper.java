package ru.practicum.sht.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.sht.dto.shopping.cart.ShoppingCartDto;
import ru.practicum.sht.model.ShoppingCart;

import java.util.HashMap;

@Component
public class ShoppingCartMapper {

    public ShoppingCartDto toDto(ShoppingCart entity) {
        if (entity == null) return null;

        return ShoppingCartDto.builder()
                .shoppingCartId(entity.getShoppingCartId())
                .products(new HashMap<>(entity.getProducts()))
                .build();
    }

}