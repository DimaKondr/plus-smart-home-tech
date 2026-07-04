package ru.practicum.sht.contract.delivery;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.sht.dto.delivery.DeliveryDto;
import ru.practicum.sht.dto.order.OrderDto;
import ru.practicum.sht.exception.delivery.NoDeliveryFoundException;
import ru.practicum.sht.exception.order.NoOrderFoundException;

import java.math.BigDecimal;
import java.util.UUID;

@FeignClient(name = "delivery", path = "/api/v1/delivery")
public interface DeliveryOperations {

    @PutMapping
    DeliveryDto planDelivery(@RequestBody @Valid DeliveryDto request);

    @PostMapping("/successful")
    void deliverySuccessful(@RequestBody UUID orderId) throws NoDeliveryFoundException, NoOrderFoundException;

    @PostMapping("/picked")
    void deliveryPicked(@RequestBody UUID orderId) throws NoDeliveryFoundException, NoOrderFoundException;

    @PostMapping("/failed")
    void deliveryFailed(@RequestBody UUID orderId) throws NoDeliveryFoundException, NoOrderFoundException;

    @PostMapping("/cost")
    BigDecimal deliveryCost(@RequestBody @Valid OrderDto order) throws NoDeliveryFoundException;

}