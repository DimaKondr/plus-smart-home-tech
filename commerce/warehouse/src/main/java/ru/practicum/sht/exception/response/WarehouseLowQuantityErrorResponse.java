package ru.practicum.sht.exception.response;

import lombok.Getter;
import ru.practicum.sht.exception.ErrorResponse;

import java.util.Map;
import java.util.UUID;

@Getter
public class WarehouseLowQuantityErrorResponse extends ErrorResponse {
    private final Map<UUID, String> missingProductsErrors;

    public WarehouseLowQuantityErrorResponse(String error, Map<UUID, String> missingProductsErrors) {
        super(error);
        this.missingProductsErrors = missingProductsErrors;
    }
}