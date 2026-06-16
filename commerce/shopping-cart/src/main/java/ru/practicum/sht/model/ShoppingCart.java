package ru.practicum.sht.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "shopping_carts")
@Data
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "shopping_cart_id")
    private UUID shoppingCartId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "shopping_cart_items",
            joinColumns = @JoinColumn(name = "shopping_cart_id")
    )
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<UUID, Long> products = new HashMap<>();

}