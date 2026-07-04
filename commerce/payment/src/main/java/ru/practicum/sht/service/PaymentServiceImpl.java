package ru.practicum.sht.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.sht.contract.order.OrderOperations;
import ru.practicum.sht.contract.shopping.store.ShoppingStoreOperations;
import ru.practicum.sht.dto.order.OrderDto;
import ru.practicum.sht.dto.payment.PaymentDto;
import ru.practicum.sht.dto.shopping.store.ProductDto;
import ru.practicum.sht.exception.order.NoOrderFoundException;
import ru.practicum.sht.exception.payment.NotEnoughInfoInOrderToCalculateException;
import ru.practicum.sht.mapper.PaymentMapper;
import ru.practicum.sht.model.Payment;
import ru.practicum.sht.model.PaymentStatus;
import ru.practicum.sht.repository.PaymentRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderOperations orderClient;
    private final ShoppingStoreOperations shoppingStoreClient;

    @Override
    public PaymentDto payment(OrderDto order) {
        if (order.getProductPrice() == null || order.getDeliveryPrice() == null || order.getTotalPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Недостаточно информации для расчета оплаты.");
        }

        BigDecimal productPrice = productCost(order);
        BigDecimal totalPrice = getTotalCost(order);
        BigDecimal feeTotal = productPrice.multiply(new BigDecimal("0.10"))
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal deliveryPrice = order.getDeliveryPrice();

        UUID paymentId = order.getPaymentId() != null ? order.getPaymentId() : UUID.randomUUID();

        Payment payment = new Payment();

        payment.setPaymentId(paymentId);
        payment.setOrderId(order.getOrderId());
        payment.setProductPrice(productPrice);
        payment.setDeliveryPrice(deliveryPrice);
        payment.setTotalPrice(totalPrice);
        payment.setFeeTotal(feeTotal);
        payment.setStatus(PaymentStatus.PENDING); // Изначальный статус

        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toDto(savedPayment);
    }

    @Override
    public BigDecimal getTotalCost(OrderDto order) {
        if (order == null || order.getDeliveryPrice() == null) {
            throw new NotEnoughInfoInOrderToCalculateException(
                    "Недостаточно информации в заказе: отсутствует стоимость доставки."
            );
        }

        BigDecimal productsCost = productCost(order);
        BigDecimal vat = productsCost.multiply(new BigDecimal("0.10")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal productsWithVat = productsCost.add(vat);
        BigDecimal deliveryPrice = order.getDeliveryPrice();

        return productsWithVat.add(deliveryPrice).setScale(2, RoundingMode.HALF_UP);

    }

    @Override
    public void paymentSuccess(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoOrderFoundException("Оплата с ID " + paymentId + " не найдена"));

        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        orderClient.payment(payment.getOrderId());
    }

    @Override
    public BigDecimal productCost(OrderDto order) {
        if (order == null || order.getProducts() == null || order.getProducts().isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException(
                    "Недостаточно информации в заказе: отсутствует список товаров."
            );
        }

        BigDecimal productTotalCost = BigDecimal.ZERO;

        for (Map.Entry<UUID, Long> entry : order.getProducts().entrySet()) {
            UUID productId = entry.getKey();
            Long quantity = entry.getValue();

            ProductDto productDto = shoppingStoreClient.getProductById(productId);

            if (productDto == null || productDto.getPrice() == null) {
                throw new NotEnoughInfoInOrderToCalculateException("Не удалось получить цену для товара с ID: "
                        + productId);
            }

            BigDecimal price = productDto.getPrice();
            BigDecimal count = BigDecimal.valueOf(quantity);

            productTotalCost = productTotalCost.add(price.multiply(count));
        }

        return productTotalCost;
    }

    @Override
    public void paymentFailed(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoOrderFoundException("Оплата с ID " + paymentId + " не найдена"));

        payment.setStatus(PaymentStatus.FAILED);
        paymentRepository.save(payment);

        orderClient.paymentFailed(payment.getOrderId());
    }

}