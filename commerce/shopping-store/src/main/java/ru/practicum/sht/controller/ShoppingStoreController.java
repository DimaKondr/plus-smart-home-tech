package ru.practicum.sht.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.sht.contract.shopping.store.ShoppingStoreOperations;
import ru.practicum.sht.dto.shopping.store.PageProductDto;
import ru.practicum.sht.dto.shopping.store.ProductCategory;
import ru.practicum.sht.dto.shopping.store.ProductDto;
import ru.practicum.sht.request.shopping.store.SetProductQuantityStateRequest;
import ru.practicum.sht.service.ProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shopping-store")
@Slf4j
@Validated
@RequiredArgsConstructor
public class ShoppingStoreController implements ShoppingStoreOperations {
    private final ProductService productService;

    /*@GetMapping
    public PageProductDto getProductPage(
            @RequestParam
                @Pattern(regexp = "LIGHTING|CONTROL|SENSORS",
                        message = "Допустимые категории: LIGHTING, CONTROL, SENSORS.")
                ProductCategory category,
            @RequestParam(defaultValue = "0")
                @PositiveOrZero Integer page,
            @RequestParam(defaultValue = "20")
                @PositiveOrZero Integer size,
            @RequestParam String[] sort
    ) {
        return null;
    }*/

    @Override
    public PageProductDto getProducts(/*ProductCategory category,*/String category, Integer page, Integer size, List<String> sort) {
        log.info("Поступил запрос на получение страницы с товарами: " +
                "категория {}, индекс страниц {}, размер страницы {}, количество параметров сортировки {}.",
                category, page, size, sort);

        List<String> checkedSort;
        if (sort == null || sort.isEmpty()) {
            checkedSort = List.of("productName,asc");
            log.info("Будут применены условия сортировки по умолчанию: {}.", checkedSort);
        } else {
            checkedSort = List.copyOf(sort);
        }

        PageProductDto dto = productService.getProducts(ProductCategory.valueOf(category), page, size, checkedSort);

        log.info("В качестве ответа передана страница с товарами: {}.", dto);

        return dto;
    }

    /*@PutMapping
    public ProductDto createProduct(
            @RequestBody
                @NotNull(message = "Добавляемый товар не может быть null")
                @Valid ProductDto dto
    ) {
        return null;
    }*/

    @Override
    public ProductDto createProduct(ProductDto dto) {
        log.info("Поступил запрос на добавление нового товара: {}.", dto);

        ProductDto createdDto = productService.createProduct(dto);

        log.info("Добавлен новый товар: {}.", createdDto);

        return createdDto;
    }

    /*@PostMapping
    public ProductDto updateProduct(
            @RequestBody
                @NotNull(message = "Добавляемый товар не может быть null")
                @Valid ProductDto dto
    ) {
        return null;
    }*/

    @Override
    public ProductDto updateProduct(ProductDto dto) {
        log.info("Поступил запрос на обновление данных товара с ID: {}.", dto.getProductId());

        ProductDto updatedDto = productService.updateProduct(dto);

        log.info("Обновлены данные товара: {}.", updatedDto);

        return updatedDto;
    }

    /*@PostMapping("/removeProductFromStore")
    public Boolean removeProduct(
            @RequestBody
                @NotNull(message = "ID удаляемого товара не может быть null")
                String productId
    ) {
        return null;
    }*/

    @Override
    public Boolean removeProduct(UUID productId) {
        log.info("Поступил запрос на удаление товара с ID: {}.", productId);

        Boolean isDone = productService.removeProduct(productId);
        if (isDone) log.info("Товар с ID: {} успешно переведен в состояние DEACTIVATE.", productId);

        return isDone;
    }

    /*@PostMapping("/quantityState")
    public Boolean setQuantityState(
            @RequestBody SetProductQuantityStateRequest quantityState
    ) {
        return null;
    }*/

    @Override
    public Boolean setQuantityState(SetProductQuantityStateRequest quantityState/*UUID productId, String quantityState*/) {
        log.info("Поступил запрос на обновление данных о статусе наличия товара с ID: {}. Данные о наличии: {}.",
                quantityState.getProductId(), quantityState.getQuantityState());
                /*productId, quantityState);*/

        /*SetProductQuantityStateRequest request = SetProductQuantityStateRequest.builder()
                .productId(productId)
                .build();*/

        Boolean isDone = productService.setQuantityState(quantityState);
        /*Boolean isDone = productService.setQuantityState(request);*/
        if (isDone) log.info("Для товара с ID: {} обновлены данные о наличии: {}.",
                quantityState.getProductId(), quantityState.getQuantityState());
                /*productId, quantityState);*/

        return isDone;
    }

    /*@GetMapping("/{productId}")
    public ProductDto getProduct(
            @PathVariable
                @NotNull(message = "ID товара не может быть null")
                String productId
    ) {
        return null;
    }*/

    @Override
    public ProductDto getProductById(UUID productId) {
        log.info("Поступил запрос на получение данных товара с ID: {}.", productId);

        ProductDto dto = productService.getProductById(productId);

        log.info("Получены данные товара: {}.", dto);

        return dto;
    }

}