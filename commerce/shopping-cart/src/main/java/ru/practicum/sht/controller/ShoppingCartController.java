package ru.practicum.sht.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.sht.contract.shopping.cart.ShoppingCartOperations;
import ru.practicum.sht.dto.shopping.cart.ShoppingCartDto;
import ru.practicum.sht.request.shopping.cart.ChangeProductQuantityRequest;
import ru.practicum.sht.service.ShoppingCartService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-cart")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ShoppingCartController implements ShoppingCartOperations {
    private final ShoppingCartService shoppingCartService;

    @Override
    public ShoppingCartDto getShoppingCart(String username) {
        log.info("Поступил запрос на получение корзины пользователя: {}.", username);

        ShoppingCartDto dto = shoppingCartService.getShoppingCart(username);

        log.info("В качестве ответа передана корзина пользователя: {}.", dto);

        return dto;
    }

    @Override
    public ShoppingCartDto putToShoppingCart(String username, Map<UUID, Long> products) {
        log.info("Поступил запрос на добавление товаров: {} в корзину пользователя: {}.", products, username);

        ShoppingCartDto dto = shoppingCartService.putToShoppingCart(username, products);

        log.info("В качестве ответа передана обновленная корзина пользователя: {}.", dto);

        return dto;
    }

    @Override
    public void removeShoppingCart(String username) {
        log.info("Поступил запрос на удаление (деактивации) корзины пользователя: {}.", username);

        shoppingCartService.removeShoppingCart(username);
    }

    @Override
    public ShoppingCartDto removeFromShoppingCart(String username, List<UUID> productIds) {
        log.info("Поступил запрос на товаров из корзины пользователя: {}.", username);

        ShoppingCartDto dto = shoppingCartService.removeFromShoppingCart(username, productIds);

        log.info("В корзине пользователя {} после удаления остались следующие товары: {}.",
                username, dto.getProducts());

        return dto;
    }

    @Override
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request) {
        log.info("Поступил запрос на изменение количества товара: {} в корзине пользователя: {}.",request, username);

        ShoppingCartDto dto = shoppingCartService.changeQuantity(username, request);

        log.info("В корзине пользователя {} после изменения количества остались следующие товары: {}.",
                username, dto.getProducts());

        return dto;
    }

}