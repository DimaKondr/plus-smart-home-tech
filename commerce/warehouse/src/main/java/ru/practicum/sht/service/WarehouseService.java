package ru.practicum.sht.service;

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

public interface WarehouseService {

    void addNewProduct(NewProductInWarehouseRequest request) throws SpecifiedProductAlreadyInWarehouseException;

    void shipProduct(ShippedToDeliveryRequest request);

    void returnProduct(Map<UUID, Long> returnItems);

    BookedProductsDto checkProduct(ShoppingCartDto dto) throws ProductInShoppingCartNotInWarehouse;

    BookedProductsDto assemblyProductForOrderFromShoppingCart(AssemblyProductsForOrderRequest request, UUID deliveryId)
            throws ProductInShoppingCartLowQuantityInWarehouseException;

    void acceptProductToWarehouse(AddProductToWarehouseRequest request) throws NoSpecifiedProductInWarehouseException;

    AddressDto getWarehouseAddress();

}