package ru.practicum.sht.repository;

import java.util.UUID;

public interface ProductWithStockShort {

    UUID getProductId();

    Boolean getFragile();

    Double getWidth();

    Double getHeight();

    Double getDepth();

    Double getWeight();

    Long getQuantity();
}