package ru.practicum.sht.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @Column(name = "payment_id")
    private UUID paymentId;

    @NotNull
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "product_price")
    private BigDecimal productPrice;

    @Column(name = "delivery_price")
    private BigDecimal deliveryPrice;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "fee_total")
    private BigDecimal feeTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;
}