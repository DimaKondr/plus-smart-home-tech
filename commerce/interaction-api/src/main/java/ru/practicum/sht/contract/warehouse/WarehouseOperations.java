package ru.practicum.sht.contract.warehouse;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.sht.dto.shopping.cart.ShoppingCartDto;
import ru.practicum.sht.dto.warehouse.AddressDto;
import ru.practicum.sht.dto.warehouse.BookedProductsDto;
import ru.practicum.sht.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.practicum.sht.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.practicum.sht.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;
import ru.practicum.sht.request.warehouse.AddProductToWarehouseRequest;
import ru.practicum.sht.request.warehouse.NewProductInWarehouseRequest;

@FeignClient(name = "warehouse", path = "/api/v1/warehouse")
public interface WarehouseOperations {

    @PutMapping
    void addNewProduct(@RequestBody @Valid NewProductInWarehouseRequest request)
            throws SpecifiedProductAlreadyInWarehouseException;

    @PostMapping("/check")
    BookedProductsDto checkProduct(@RequestBody @Valid ShoppingCartDto dto)
            throws ProductInShoppingCartLowQuantityInWarehouseException;

    @PostMapping("/add")
    void acceptProductToWarehouse(@RequestBody @Valid AddProductToWarehouseRequest request)
            throws NoSpecifiedProductInWarehouseException;

    @GetMapping("/address")
    AddressDto getAddress();

}