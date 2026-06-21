package ru.practicum.sht.service;

import ru.practicum.sht.dto.shopping.cart.ShoppingCartDto;
import ru.practicum.sht.exception.shopping.cart.NoProductsInShoppingCartException;
import ru.practicum.sht.exception.shopping.cart.NotAuthorizedUserException;
import ru.practicum.sht.request.shopping.cart.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCart(String username) throws NotAuthorizedUserException;

    ShoppingCartDto putToShoppingCart(String username, Map<UUID, Long> products) throws NotAuthorizedUserException;

    void removeShoppingCart(String username) throws NotAuthorizedUserException;

    ShoppingCartDto removeFromShoppingCart(String username, List<UUID> productIds)
            throws NoProductsInShoppingCartException, NotAuthorizedUserException;

    ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request)
            throws NoProductsInShoppingCartException, NotAuthorizedUserException;

}