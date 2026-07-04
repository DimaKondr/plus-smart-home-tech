package ru.practicum.sht.contract.warehouse;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.sht.dto.shopping.cart.ShoppingCartDto;
import ru.practicum.sht.dto.warehouse.AddressDto;
import ru.practicum.sht.dto.warehouse.BookedProductsDto;
import ru.practicum.sht.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.practicum.sht.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.practicum.sht.exception.warehouse.ProductInShoppingCartNotInWarehouse;
import ru.practicum.sht.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;
import ru.practicum.sht.request.warehouse.AddProductToWarehouseRequest;
import ru.practicum.sht.request.warehouse.AssemblyProductsForOrderRequest;
import ru.practicum.sht.request.warehouse.NewProductInWarehouseRequest;
import ru.practicum.sht.request.warehouse.ShippedToDeliveryRequest;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseOperations {

    @PutMapping
    void addNewProduct(@RequestBody @Valid NewProductInWarehouseRequest request)
            throws SpecifiedProductAlreadyInWarehouseException;

    @PostMapping("/shipped")
    void shippedToDelivery(@RequestBody @Valid ShippedToDeliveryRequest request);

    @PostMapping("/return")
    void returnProduct(@RequestBody Map<UUID, Long> returnItems);

    @PostMapping("/check")
    BookedProductsDto checkProduct(@RequestBody @Valid ShoppingCartDto dto)
            throws ProductInShoppingCartNotInWarehouse;

    @PostMapping("/assembly")
    BookedProductsDto assemblyProductForOrderFromShoppingCart(
            @RequestBody @Valid AssemblyProductsForOrderRequest request,
            @RequestHeader("X-Delivery-ID") UUID deliveryId
    ) throws ProductInShoppingCartLowQuantityInWarehouseException;

    @PostMapping("/add")
    void acceptProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest request)
            throws NoSpecifiedProductInWarehouseException;

    @GetMapping("/address")
    AddressDto getWarehouseAddress();

}