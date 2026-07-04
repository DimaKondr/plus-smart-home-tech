package ru.practicum.sht.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.sht.dto.order.OrderState;

import java.math.BigDecimal;
import java.util.*;

@Entity
@Table(name = "orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "shopping_cart_id", nullable = false)
    private UUID shoppingCartId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "order_items",
            joinColumns = @JoinColumn(name = "order_id")
    )
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity", nullable = false)
    private Map<UUID, Long> products = new HashMap<>();

    @Column(name = "payment_id", nullable = false)
    private UUID paymentId;

    @Column(name = "delivery_id", nullable = false)
    private UUID deliveryId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false)
    private OrderState state;

    @Column(name = "delivery_weight", nullable = false)
    private Double deliveryWeight;

    @Column(name = "delivery_volume", nullable = false)
    private Double deliveryVolume;

    @Column(name = "fragile", nullable = false)
    private Boolean fragile;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "product_price", nullable = false)
    private BigDecimal productPrice;

    @Column(name = "delivery_price", nullable = false)
    private BigDecimal deliveryPrice;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "house", nullable = false)
    private String house;

    @Column(name = "flat", nullable = false)
    private String flat;
}