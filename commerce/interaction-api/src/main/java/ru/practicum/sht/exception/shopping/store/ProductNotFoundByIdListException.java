package ru.practicum.sht.exception.shopping.store;

import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter
@ToString
public class ProductNotFoundByIdListException extends RuntimeException {
    private final List<UUID> missingProductsIdList;

    public ProductNotFoundByIdListException(String message, List<UUID> missingProductsIdList) {
        super(message);
        this.missingProductsIdList = missingProductsIdList;
    }
}