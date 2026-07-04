package ru.practicum.sht.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.sht.dto.delivery.DeliveryState;

import java.util.UUID;

@Entity
@Table(name = "delivery")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {

    @Id
    @Column(name = "delivery_id", nullable = false)
    private UUID deliveryId;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_state", nullable = false)
    private DeliveryState state;

    @Column(name = "delivery_weight")
    private Double deliveryWeight;

    @Column(name = "delivery_volume")
    private Double deliveryVolume;

    @Column(name = "fragile")
    private Boolean fragile;

    @Column(name = "from_country", nullable = false)
    private String fromCountry;

    @Column(name = "from_city", nullable = false)
    private String fromCity;

    @Column(name = "from_street", nullable = false)
    private String fromStreet;

    @Column(name = "from_house", nullable = false)
    private String fromHouse;

    @Column(name = "from_flat", nullable = false)
    private String fromFlat;

    @Column(name = "to_country", nullable = false)
    private String toCountry;

    @Column(name = "to_city", nullable = false)
    private String toCity;

    @Column(name = "to_street", nullable = false)
    private String toStreet;

    @Column(name = "to_house", nullable = false)
    private String toHouse;

    @Column(name = "to_flat", nullable = false)
    private String toFlat;
}