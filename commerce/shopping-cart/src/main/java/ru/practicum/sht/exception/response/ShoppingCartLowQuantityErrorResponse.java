package ru.practicum.sht.exception.response;

import lombok.Getter;
import ru.practicum.sht.exception.ErrorResponse;

import java.util.Map;
import java.util.UUID;

@Getter
public class ShoppingCartLowQuantityErrorResponse extends ErrorResponse {
    private final Map<UUID, String> missingProductsErrors;

    public ShoppingCartLowQuantityErrorResponse(String error, Map<UUID, String> missingProductsErrors) {
        super(error);
        this.missingProductsErrors = missingProductsErrors;
    }
}