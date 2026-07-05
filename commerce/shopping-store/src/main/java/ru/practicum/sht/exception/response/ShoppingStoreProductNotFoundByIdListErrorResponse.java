package ru.practicum.sht.exception.response;

import lombok.Getter;
import ru.practicum.sht.exception.ErrorResponse;

import java.util.List;
import java.util.UUID;

@Getter
public class ShoppingStoreProductNotFoundByIdListErrorResponse extends ErrorResponse {
    private final List<UUID> missingProductsIdList;

    public ShoppingStoreProductNotFoundByIdListErrorResponse(String message, List<UUID> missingProductsIdList) {
        super(message);
        this.missingProductsIdList = missingProductsIdList;
    }
}