package ru.practicum.sht.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.sht.contract.payment.PaymentOperations;
import ru.practicum.sht.dto.order.OrderDto;
import ru.practicum.sht.dto.payment.PaymentDto;
import ru.practicum.sht.service.PaymentService;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payment")
@Slf4j
@Validated
@RequiredArgsConstructor
public class PaymentController implements PaymentOperations {
    private final PaymentService paymentService;

    @Override
    public PaymentDto payment(OrderDto order) {
        log.info("Поступил запрос на формирование оплаты для заказа: {}.", order);

        PaymentDto answer = paymentService.payment(order);

        log.info("Сформированы данные по оплате: {}.", answer);

        return answer;
    }

    @Override
    public BigDecimal getTotalCost(OrderDto order) {
        log.info("Поступил запрос на расчет общей стоимости заказа: {}.", order);

        BigDecimal answer = paymentService.getTotalCost(order);

        log.info("Рассчитана общая стоимость заказа с ID: {}. Стоимость заказа составила: {}", order, answer);

        return answer;
    }

    @Override
    public void paymentSuccess(UUID paymentId) {
        log.info("Поступил запрос на сохранение успешного статуса платежа с ID: {}.", paymentId);

        paymentService.paymentSuccess(paymentId);

        log.info("Статус платежа с ID: {} изменен на успешный.", paymentId);
    }

    @Override
    public BigDecimal productCost(OrderDto order) {
        log.info("Поступил запрос на расчет стоимости товаров в заказе: {}.", order);

        BigDecimal answer = paymentService.productCost(order);

        log.info("Рассчитана стоимость товаров в заказе с ID: {}. Стоимость товаров составила: {}", order, answer);

        return answer;
    }

    @Override
    public void paymentFailed(UUID paymentId) {
        log.info("Поступил запрос на сохранение неудачного статуса платежа с ID: {}.", paymentId);

        paymentService.paymentFailed(paymentId);

        log.info("Статус платежа с ID: {} изменен на неудачный.", paymentId);
    }

}