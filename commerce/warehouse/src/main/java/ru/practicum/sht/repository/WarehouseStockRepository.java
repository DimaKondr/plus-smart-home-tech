package ru.practicum.sht.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.sht.model.WarehouseStock;

import java.util.UUID;

@Repository
public interface WarehouseStockRepository extends JpaRepository<WarehouseStock, UUID> {
}