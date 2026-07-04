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
import ru.practicum.sht.request.warehouse.AssemblyProductsForOrderRequest;
import ru.practicum.sht.request.warehouse.NewProductInWarehouseRequest;
import ru.practicum.sht.request.warehouse.ShippedToDeliveryRequest;
import ru.practicum.sht.service.WarehouseService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouse")
@Slf4j
@Validated
@RequiredArgsConstructor
public class WarehouseController implements WarehouseOperations {
    private final WarehouseService warehouseService;

    @Override
    public void addNewProduct(NewProductInWarehouseRequest request) {
        log.info("Поступил запрос на добавление нового типа товара на склад: {}.", request);

        warehouseService.addNewProduct(request);

        log.info("Новый тип товара {} добавлен на склад.", request);
    }

    @Override
    public void shippedToDelivery(ShippedToDeliveryRequest request) {
        log.info("Начат процесс отправки в доставку заказа: {}. ID Доставки: {}.",
                request.getOrderId(), request.getDeliveryId());

        warehouseService.shipProduct(request);

        log.info("Заказ: {} передан в доставку. ID Доставки: {}.", request.getOrderId(), request.getDeliveryId());
    }

    @Override
    public void returnProduct(Map<UUID, Long> returnItems) {
        log.info("Поступил запрос на возврат товаров: {}.", returnItems);

        warehouseService.returnProduct(returnItems);

        log.info("Возвращен товар: {}.", returnItems);
    }

    @Override
    public BookedProductsDto checkProduct(ShoppingCartDto dto) {
        log.info("Поступил запрос на проверку наличия товаров на складе: {}.", dto);

        BookedProductsDto response = warehouseService.checkProduct(dto);

        log.info("Все запрашиваемые товары имеются в необходимом количестве на складе: {}.", response);

        return response;
    }

    @Override
    public BookedProductsDto assemblyProductForOrderFromShoppingCart(
            AssemblyProductsForOrderRequest request,
            UUID deliveryId
    ) {
        log.info("Поступил запрос на сборку товара для заказа с ID: {}. Данные запроса: {}.", deliveryId, request);

        BookedProductsDto response = warehouseService.assemblyProductForOrderFromShoppingCart(request, deliveryId);

        log.info("Все товары в заказе собраны на складе: {}.", response);

        return response;
    }

    @Override
    public void acceptProductToWarehouse(AddProductToWarehouseRequest request) {
        log.info("Поступил запрос на пополнение складских запасов товара с ID: {} на {} единиц.",
                request.getProductId(), request.getQuantity());

        warehouseService.acceptProductToWarehouse(request);

        log.info("Количество товара с ID: {} успешно увеличено на {} единиц.",
                request.getProductId(), request.getQuantity());
    }

    @Override
    public AddressDto getWarehouseAddress() {
        log.info("Поступил запрос на получение адреса склада.");

        AddressDto dto = warehouseService.getWarehouseAddress();

        log.info("Предоставлен адрес склада: {}.", dto);

        return dto;
    }

}