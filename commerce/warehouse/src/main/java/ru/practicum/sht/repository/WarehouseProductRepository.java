package ru.practicum.sht.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.sht.model.WarehouseProduct;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface WarehouseProductRepository extends JpaRepository<WarehouseProduct, UUID> {

    @Query("select " +
               "p.productId as productId, " +
               "p.fragile as fragile, " +
               "p.width as width, " +
               "p.height as height, " +
               "p.depth as depth, " +
               "p.weight as weight, " +
               "s.quantity as quantity " +
           "from WarehouseProduct p " +
           "left join WarehouseStock s on p.productId = s.productId " +
           "where p.productId in :productIds")
    List<ProductWithStockShort> findAllProductsWithStock(@Param("productIds") Set<UUID> productIds);

}