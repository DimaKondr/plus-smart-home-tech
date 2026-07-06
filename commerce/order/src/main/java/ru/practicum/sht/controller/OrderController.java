package ru.practicum.sht.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.sht.contract.order.OrderOperations;
import ru.practicum.sht.request.order.CreateNewOrderRequest;
import ru.practicum.sht.dto.order.OrderDto;
import ru.practicum.sht.request.order.ProductReturnRequest;
import ru.practicum.sht.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order")
@Slf4j
@Validated
@RequiredArgsConstructor
public class OrderController implements OrderOperations {
    private final OrderService orderService;

    @Override
    public List<OrderDto> getClientOrders(String username) {
        log.info("Поступил запрос на получение списка заказов пользователя: {}.", username);

        List<OrderDto> answer = orderService.getClientOrders(username);

        log.info("По запросу предоставлен список заказов пользователя: {}. Заказы: {}.", username, answer);

        return answer;
    }

    @Override
    public OrderDto createNewOrder(CreateNewOrderRequest request, String username) {
        log.info("Поступил запрос на добавление нового заказа: {}.", request);

        OrderDto answer = orderService.createNewOrder(request, username);

        log.info("Добавлен новый заказ: {}.", answer);

        return answer;
    }

    @Override
    public OrderDto productReturn(ProductReturnRequest request) {
        log.info("Поступил запрос на возврат товаров заказа: {}.", request);

        OrderDto answer = orderService.productReturn(request);

        log.info("Возвращен товар по заказу: {}.", answer);

        return answer;
    }

    @Override
    public OrderDto payment(UUID orderId) {
        log.info("Начат процесс проведения оплаты заказа с ID: {}.", orderId);

        OrderDto answer = orderService.payment(orderId);

        log.info("Произведена оплата заказа: {}.", answer);

        return answer;
    }

    @Override
    public OrderDto paymentFailed(UUID orderId) {
        log.info("Возникли проблемы с оплатой заказа с ID: {}.", orderId);

        OrderDto answer = orderService.paymentFailed(orderId);

        log.info("Не удалось произвести оплату заказа: {}.", answer);

        return answer;
    }

    @Override
    public OrderDto delivery(UUID orderId) {
        log.info("Начат процесс доставки заказа с ID: {}.", orderId);

        OrderDto answer = orderService.delivery(orderId);

        log.info("Заказ: {} передан в доставку.", answer);

        return answer;
    }

    @Override
    public OrderDto deliveryFailed(UUID orderId) {
        log.info("Возникли проблемы с доставкой заказа с ID: {}.", orderId);

        OrderDto answer = orderService.deliveryFailed(orderId);

        log.info("Не удалось произвести доставку заказа: {}.", answer);

        return answer;
    }

    @Override
    public OrderDto complete(UUID orderId) {
        log.info("Начат процесс завершения заказа с ID: {}.", orderId);

        OrderDto answer = orderService.complete(orderId);

        log.info("Заказ: {} завершен.", answer);

        return answer;
    }

    @Override
    public OrderDto calculateTotalCost(UUID orderId) {
        log.info("Начат процесс расчета общей стоимости заказа с ID: {}.", orderId);

        OrderDto answer = orderService.calculateTotalCost(orderId);

        log.info("Рассчитана общая стоимость заказа: {}.", answer);

        return answer;
    }

    @Override
    public OrderDto calculateDeliveryCost(UUID orderId) {
        log.info("Начат процесс расчета стоимости доставки заказа с ID: {}.", orderId);

        OrderDto answer = orderService.calculateDeliveryCost(orderId);

        log.info("Рассчитана стоимость доставки заказа: {}.", answer);

        return answer;
    }

    @Override
    public OrderDto assembly(UUID orderId) {
        log.info("Начат процесс сборки заказа с ID: {}.", orderId);

        OrderDto answer = orderService.assembly(orderId);

        log.info("Заказ: {} собран.", answer);

        return answer;
    }

    @Override
    public OrderDto assemblyFailed(UUID orderId) {
        log.info("Возникли проблемы со сборкой заказа с ID: {}.", orderId);

        OrderDto answer = orderService.assemblyFailed(orderId);

        log.info("Не удалось произвести сборку заказа: {}.", answer);

        return answer;
    }

}