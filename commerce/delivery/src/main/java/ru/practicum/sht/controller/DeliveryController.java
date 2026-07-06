package ru.practicum.sht.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.sht.contract.delivery.DeliveryOperations;
import ru.practicum.sht.dto.delivery.DeliveryDto;
import ru.practicum.sht.dto.order.OrderDto;
import ru.practicum.sht.service.DeliveryService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery")
@Slf4j
@Validated
@RequiredArgsConstructor
public class DeliveryController implements DeliveryOperations {
    private final DeliveryService deliveryService;

    @Override
    public DeliveryDto planDelivery(DeliveryDto request) {
        log.info("Поступил запрос на добавление новой доставки: {}.", request);

        DeliveryDto answer = deliveryService.planDelivery(request);

        log.info("Добавлена новая доставка: {}.", answer);

        return answer;
    }

    @Override
    public void deliverySuccessful(UUID orderId) {
        log.info("Поступил запрос на назначение успешного статуса доставки заказа с ID: {}.", orderId);

        deliveryService.markAsSuccessful(orderId);

        log.info("Установлен успешный статус доставки заказа с ID: {}.", orderId);
    }

    @Override
    public void deliveryPicked(UUID orderId) {
        log.info("Поступил запрос на назначение статуса о передаче в доставку заказа с ID: {}.", orderId);

        deliveryService.markAsPicked(orderId);

        log.info("Для заказа с ID: {} установлен статус о передаче в доставку.", orderId);
    }

    @Override
    public void deliveryFailed(UUID orderId) {
        log.info("Возникли проблемы с доставкой заказа с ID: {}.", orderId);

        deliveryService.markAsFailed(orderId);

        log.info("Не удалось произвести доставку заказа с ID: {}.", orderId);
    }

    @Override
    public BigDecimal deliveryCost(OrderDto order) {
        log.info("Начат процесс расчета стоимости доставки заказа: {}.", order);

        BigDecimal answer = deliveryService.deliveryCost(order);

        log.info("Рассчитана стоимость доставки заказа: {}. Стоимость доставки составила: {}.", order, answer);

        return answer;
    }

}