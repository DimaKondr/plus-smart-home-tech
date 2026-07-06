package ru.practicum.sht.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "order_assemblies")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderAssembly {

    @Id
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "delivery_id", nullable = false)
    private UUID deliveryId;
}