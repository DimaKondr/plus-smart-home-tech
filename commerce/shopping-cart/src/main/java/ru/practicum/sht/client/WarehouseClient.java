package ru.practicum.sht.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.sht.dto.shopping.cart.ShoppingCartDto;
import ru.practicum.sht.dto.warehouse.BookedProductsDto;
import ru.practicum.sht.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouseException;

//@FeignClient(name = "warehouse"/*, url = "${warehouse.service.url:http://localhost:8080/api/v1/warehouse}"*/)
public interface WarehouseClient {

    /*@PostMapping("/check")
    BookedProductsDto checkProduct(@RequestBody ShoppingCartDto dto)
            throws ProductInShoppingCartLowQuantityInWarehouseException;*/

}