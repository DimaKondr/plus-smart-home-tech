package ru.practicum.sht.service;

import ru.practicum.sht.dto.delivery.DeliveryDto;
import ru.practicum.sht.dto.order.OrderDto;
import ru.practicum.sht.exception.delivery.NoDeliveryFoundException;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {

    DeliveryDto planDelivery(DeliveryDto request);

    void markAsSuccessful(UUID orderId) throws NoDeliveryFoundException;

    void markAsPicked(UUID orderId) throws NoDeliveryFoundException;

    void markAsFailed(UUID orderId) throws NoDeliveryFoundException;

    BigDecimal deliveryCost(OrderDto order) throws NoDeliveryFoundException;

}