package ru.practicum.sht.dto.shopping.store;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder
public class ProductDto {
    //String productId;
    UUID productId;

    @NotNull(message = "Название товара не должно быть null")
    String productName;

    @NotNull(message = "Описание товара не должно быть null")
    String description;
    String imageSrc;

    @NotNull(message = "Состояние остатка товара не должно быть null")
    QuantityState quantityState;

    @NotNull(message = "Статус товара не должен быть null")
    ProductState productState;

    @NotNull(message = "Категория товара не должна быть null")
    ProductCategory productCategory;

    @DecimalMin(value = "1.00", message = "Цена не может быть меньше 1")
    @NotNull(message = "Цена товара не должна быть null")
    BigDecimal price;
}