package ru.practicum.sht.request.shopping.cart;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Value
@Builder
//@Getter
//@Setter
//@ToString
//@EqualsAndHashCode
//@AllArgsConstructor
public class ChangeProductQuantityRequest {

    @NotNull(message = "ID товара не должно быть null")
    //String productId;
    UUID productId;

    @NotNull(message = "Измененное количество товара не должно быть null")
    Long newQuantity;
}