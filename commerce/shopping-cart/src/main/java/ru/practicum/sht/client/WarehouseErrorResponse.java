package ru.practicum.sht.client;

import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
public class WarehouseErrorResponse {
    private final String error;
    private final Map<UUID, String> missingProducts;
}