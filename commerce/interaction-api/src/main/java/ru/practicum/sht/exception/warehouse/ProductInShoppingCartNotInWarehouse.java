package ru.practicum.sht.exception.warehouse;

import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.UUID;

@Getter
@ToString
public class ProductInShoppingCartNotInWarehouse extends RuntimeException {
    private final Map<UUID, String> missingProductsErrors;

    public ProductInShoppingCartNotInWarehouse(
            String message,
            Map<UUID, String> missingProductsErrors
    ) {
        super(message);
        this.missingProductsErrors = missingProductsErrors;
    }

}