package ru.practicum.sht.contract.shopping.store;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;
import ru.practicum.sht.dto.shopping.store.PageProductDto;
import ru.practicum.sht.dto.shopping.store.ProductDto;
import ru.practicum.sht.exception.shopping.store.ProductNotFoundException;
import ru.practicum.sht.request.shopping.store.SetProductQuantityStateRequest;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "shopping-store", path = "/api/v1/shopping-store")
public interface ShoppingStoreOperations {

    @GetMapping
    PageProductDto getProducts(
            @RequestParam
                @Pattern(regexp = "LIGHTING|CONTROL|SENSORS",
                         message = "Допустимые категории: LIGHTING, CONTROL, SENSORS.")
                String category,
            @RequestParam(defaultValue = "0")
                @PositiveOrZero Integer page,
            @RequestParam(defaultValue = "20")
                @PositiveOrZero Integer size,
            @RequestParam(required = false) List<String> sort
    );

    @PutMapping
    ProductDto createProduct(
            @RequestBody
                @NotNull(message = "Данные добавляемого товара не могут быть null")
                @Valid ProductDto dto
    );

    @PostMapping
    ProductDto updateProduct(
            @RequestBody
                @NotNull(message = "Данные обновляемого товара не могут быть null")
                @Valid ProductDto dto
    ) throws ProductNotFoundException;

    @PostMapping("/removeProductFromStore")
    Boolean removeProduct(
            @RequestBody
                @NotNull(message = "ID товара не может быть null")
                UUID productId
    ) throws ProductNotFoundException;

    @PostMapping("/quantityState")
    Boolean setQuantityState(
            @SpringQueryMap
                @Valid SetProductQuantityStateRequest request
    ) throws ProductNotFoundException;

    @GetMapping("/{productId}")
    ProductDto getProductById(
            @PathVariable
                @NotNull(message = "ID товара не может быть null")
                UUID productId
    ) throws ProductNotFoundException;

}