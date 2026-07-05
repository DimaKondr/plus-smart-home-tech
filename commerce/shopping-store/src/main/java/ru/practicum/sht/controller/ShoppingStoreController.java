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

    @Override
    public PageProductDto getProducts(String category, Integer page, Integer size, List<String> sort) {
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

    @Override
    public ProductDto createProduct(ProductDto dto) {
        log.info("Поступил запрос на добавление нового товара: {}.", dto);

        ProductDto createdDto = productService.createProduct(dto);

        log.info("Добавлен новый товар: {}.", createdDto);

        return createdDto;
    }

    @Override
    public ProductDto updateProduct(ProductDto dto) {
        log.info("Поступил запрос на обновление данных товара с ID: {}.", dto.getProductId());

        ProductDto updatedDto = productService.updateProduct(dto);

        log.info("Обновлены данные товара: {}.", updatedDto);

        return updatedDto;
    }

    @Override
    public Boolean removeProduct(UUID productId) {
        log.info("Поступил запрос на удаление товара с ID: {}.", productId);

        Boolean isDone = productService.removeProduct(productId);
        if (isDone) log.info("Товар с ID: {} успешно переведен в состояние DEACTIVATE.", productId);

        return isDone;
    }

    @Override
    public Boolean setQuantityState(SetProductQuantityStateRequest quantityState) {
        log.info("Поступил запрос на обновление данных о статусе наличия товара с ID: {}. Данные о наличии: {}.",
                quantityState.getProductId(), quantityState.getQuantityState());

        Boolean isDone = productService.setQuantityState(quantityState);
        if (isDone) log.info("Для товара с ID: {} обновлены данные о наличии: {}.",
                quantityState.getProductId(), quantityState.getQuantityState());

        return isDone;
    }

    @Override
    public ProductDto getProductById(UUID productId) {
        log.info("Поступил запрос на получение данных товара с ID: {}.", productId);

        ProductDto dto = productService.getProductById(productId);

        log.info("Получены данные товара: {}.", dto);

        return dto;
    }

    @Override
    public List<ProductDto> getProductsByIdList(List<UUID> productsIdList) {
        log.info("Поступил запрос на получение данных некоторых товаров по списку ID: {}.", productsIdList);

        List<ProductDto> dtoList = productService.getProductsByIdList(productsIdList);

        log.info("Получены данные товаров по списку: {}.", dtoList);

        return dtoList;
    }

}