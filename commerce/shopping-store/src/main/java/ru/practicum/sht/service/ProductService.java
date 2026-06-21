package ru.practicum.sht.service;

import ru.practicum.sht.dto.shopping.store.PageProductDto;
import ru.practicum.sht.dto.shopping.store.ProductCategory;
import ru.practicum.sht.dto.shopping.store.ProductDto;
import ru.practicum.sht.exception.shopping.store.ProductNotFoundException;
import ru.practicum.sht.request.shopping.store.SetProductQuantityStateRequest;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    PageProductDto getProducts(ProductCategory category, int page, int size, List<String> sortParameters);

    ProductDto createProduct(ProductDto dto);

    ProductDto updateProduct(ProductDto dto) throws ProductNotFoundException;

    Boolean removeProduct(UUID productId) throws ProductNotFoundException;

    Boolean setQuantityState(SetProductQuantityStateRequest quantityState) throws ProductNotFoundException;

    ProductDto getProductById(UUID productId) throws ProductNotFoundException;

}