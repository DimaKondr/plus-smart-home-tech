package ru.practicum.sht.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.sht.dto.shopping.store.ProductCategory;
import ru.practicum.sht.dto.shopping.store.ProductState;
import ru.practicum.sht.model.Product;

import java.util.UUID;

@Repository
public interface ShoppingStoreRepository extends JpaRepository<Product, UUID> {

    Page<Product> findByProductCategoryAndProductState(
            ProductCategory productCategory,
            ProductState productState,
            Pageable pageable
    );

}