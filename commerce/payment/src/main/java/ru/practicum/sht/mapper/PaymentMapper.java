package ru.practicum.sht.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.sht.dto.payment.PaymentDto;
import ru.practicum.sht.model.Payment;

@Component
public class PaymentMapper {

    public PaymentDto toDto(Payment entity) {
        return PaymentDto.builder()
                .paymentId(entity.getPaymentId())
                .totalPayment(entity.getTotalPrice())
                .deliveryTotal(entity.getDeliveryPrice())
                .feeTotal(entity.getFeeTotal())
                .build();
    }

}