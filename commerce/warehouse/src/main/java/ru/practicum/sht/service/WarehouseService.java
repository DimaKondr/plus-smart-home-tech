package ru.practicum.sht.service;

import ru.practicum.sht.dto.shopping.cart.ShoppingCartDto;
import ru.practicum.sht.dto.warehouse.AddressDto;
import ru.practicum.sht.dto.warehouse.BookedProductsDto;
import ru.practicum.sht.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.practicum.sht.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.practicum.sht.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;
import ru.practicum.sht.request.warehouse.AddProductToWarehouseRequest;
import ru.practicum.sht.request.warehouse.NewProductInWarehouseRequest;

public interface WarehouseService {

    void addNewProduct(NewProductInWarehouseRequest request) throws SpecifiedProductAlreadyInWarehouseException;

    BookedProductsDto checkProduct(ShoppingCartDto dto) throws ProductInShoppingCartLowQuantityInWarehouseException;

    void acceptProductToWarehouse(AddProductToWarehouseRequest request) throws NoSpecifiedProductInWarehouseException;

    AddressDto getAddress();
}