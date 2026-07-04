package ru.practicum.sht.service;

import ru.practicum.sht.dto.order.OrderDto;
import ru.practicum.sht.dto.payment.PaymentDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface PaymentService {

    PaymentDto payment(OrderDto order);

    BigDecimal getTotalCost(OrderDto order);

    void paymentSuccess(UUID paymentId);

    BigDecimal productCost(OrderDto order);

    void paymentFailed(UUID paymentId);
}