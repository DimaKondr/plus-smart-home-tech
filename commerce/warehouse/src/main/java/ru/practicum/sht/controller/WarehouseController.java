package ru.practicum.sht.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.sht.contract.warehouse.WarehouseOperations;
import ru.practicum.sht.dto.shopping.cart.ShoppingCartDto;
import ru.practicum.sht.request.warehouse.AddProductToWarehouseRequest;
import ru.practicum.sht.dto.warehouse.AddressDto;
import ru.practicum.sht.dto.warehouse.BookedProductsDto;
import ru.practicum.sht.request.warehouse.NewProductInWarehouseRequest;
import ru.practicum.sht.service.WarehouseService;

@RestController
@RequestMapping("/api/v1/warehouse")
@Slf4j
@Validated
@RequiredArgsConstructor
public class WarehouseController implements WarehouseOperations {
    private final WarehouseService warehouseService;

    /*@PutMapping
    public void addNewProduct(
            @RequestBody
                @NotNull(message = "Добавляемый новый товар не может быть null")
                @Valid NewProductInWarehouseRequest request
    ) {

    }*/

    @Override
    public void addNewProduct(NewProductInWarehouseRequest request) {
        log.info("Поступил запрос на добавление нового типа товара на склад: {}.", request);

        warehouseService.addNewProduct(request);

        log.info("Новый тип товара {} добавлен на склад.", request);
    }

    /*@PostMapping("/check")
    public BookedProductsDto checkProduct(
            @RequestBody
                @NotNull(message = "Проверяемая корзина товаров не может быть null")
                @Valid ShoppingCartDto dto
    ) {
        return null;
    }*/

    @Override
    public BookedProductsDto checkProduct(ShoppingCartDto dto) {
        log.info("Поступил запрос на проверку наличия товаров на складе: {}.", dto);

        BookedProductsDto response = warehouseService.checkProduct(dto);

        log.info("Все запрашиваемые товары имеются в необходимом количестве на складе: {}.", response);

        return response;
    }

    /*@PostMapping("/add")
    public BookedProductsDto acceptProductToWarehouse(
            @RequestBody
                @NotNull(message = "Запрос на добавление определенного количества товара не может быть null")
                @Valid AddProductToWarehouseRequest request
    ) {
        return null;
    }*/

    @Override
    public void acceptProductToWarehouse(AddProductToWarehouseRequest request) {
        log.info("Поступил запрос на пополнение складских запасов товара с ID: {} на {} единиц.",
                request.getProductId(), request.getQuantity());

        warehouseService.acceptProductToWarehouse(request);

        log.info("Количество товара с ID: {} успешно увеличено на {} единиц.",
                request.getProductId(), request.getQuantity());
    }

    /*@GetMapping("/address")
    public AddressDto getAddress() {
        return null;
    }*/

    @Override
    public AddressDto getAddress() {
        log.info("Поступил запрос на получение адреса склада.");

        AddressDto dto = warehouseService.getAddress();

        log.info("Предоставлен адрес склада: {}.", dto);

        return dto;
    }

}