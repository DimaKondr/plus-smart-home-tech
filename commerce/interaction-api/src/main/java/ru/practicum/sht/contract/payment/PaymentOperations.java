package ru.practicum.sht.contract.payment;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.sht.dto.order.OrderDto;
import ru.practicum.sht.dto.payment.PaymentDto;
import ru.practicum.sht.exception.order.NoOrderFoundException;
import ru.practicum.sht.exception.payment.NotEnoughInfoInOrderToCalculateException;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "payment", path = "/api/v1/payment")
public interface PaymentOperations {

    @PostMapping
    PaymentDto payment(@RequestBody @Valid OrderDto order) throws NotEnoughInfoInOrderToCalculateException;

    @PostMapping("/totalCost")
    BigDecimal getTotalCost(@RequestBody OrderDto order) throws NotEnoughInfoInOrderToCalculateException;

    @PostMapping("/refund")
    void paymentSuccess(@RequestBody UUID paymentId) throws NoOrderFoundException;

    @PostMapping("/productCost")
    BigDecimal productCost(@RequestBody @Valid OrderDto order) throws NotEnoughInfoInOrderToCalculateException;

    @PostMapping("/failed")
    void paymentFailed(@RequestBody UUID paymentId) throws NoOrderFoundException;
}