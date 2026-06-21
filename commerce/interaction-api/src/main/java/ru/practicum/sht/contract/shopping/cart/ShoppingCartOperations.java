package ru.practicum.sht.contract.shopping.cart;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.sht.dto.shopping.cart.ShoppingCartDto;
import ru.practicum.sht.exception.shopping.cart.NoProductsInShoppingCartException;
import ru.practicum.sht.exception.shopping.cart.NotAuthorizedUserException;
import ru.practicum.sht.request.shopping.cart.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@FeignClient(name = "shopping-cart", path = "/api/v1/shopping-cart")
public interface ShoppingCartOperations {

    @GetMapping
    ShoppingCartDto getShoppingCart(
            @RequestParam
                @NotBlank(message = "Имя пользователя не может быть null и пустым")
                String username
    ) throws NotAuthorizedUserException;

    @PutMapping
    ShoppingCartDto putToShoppingCart(
            @RequestParam
                @NotBlank(message = "Имя пользователя не может быть null и пустым")
                String username,
            @RequestBody Map<UUID, Long> products
    ) throws NotAuthorizedUserException;

    @DeleteMapping
    void removeShoppingCart(
            @RequestParam
                @NotBlank(message = "Имя пользователя не может быть null и пустым")
                String username
    ) throws NotAuthorizedUserException;

    @PostMapping("/remove")
    ShoppingCartDto removeFromShoppingCart(
            @RequestParam
                @NotBlank(message = "Имя пользователя не может быть null и пустым")
                String username,
            @RequestBody List<UUID> productIds
    ) throws NoProductsInShoppingCartException, NotAuthorizedUserException;

    @PostMapping("/change-quantity")
    ShoppingCartDto changeQuantity(
            @RequestParam
                @NotBlank(message = "Имя пользователя не может быть null и пустым")
                String username,
            @RequestBody
                @Valid ChangeProductQuantityRequest request
    ) throws NoProductsInShoppingCartException, NotAuthorizedUserException;

}