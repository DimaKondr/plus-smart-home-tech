package ru.practicum.sht.request.shopping.store;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.practicum.sht.dto.shopping.store.QuantityState;

import java.util.UUID;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class SetProductQuantityStateRequest {

    @NotNull(message = "ID товара не должно быть null")
    UUID productId;

    @NotNull(message = "Состояние остатка товара не должно быть null")
    QuantityState quantityState;
}