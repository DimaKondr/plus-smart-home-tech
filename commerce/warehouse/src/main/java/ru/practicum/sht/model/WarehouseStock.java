package ru.practicum.sht.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "warehouse_stocks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseStock {

    @Id
    @Column(name = "product_id")
    private UUID productId;

    @Column(nullable = false)
    private Long quantity;
}