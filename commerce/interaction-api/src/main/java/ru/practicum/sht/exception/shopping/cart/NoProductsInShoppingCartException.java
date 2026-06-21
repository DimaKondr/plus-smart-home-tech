package ru.practicum.sht.exception.shopping.cart;

public class NoProductsInShoppingCartException extends RuntimeException {
    public NoProductsInShoppingCartException(String message) {
        super(message);
    }
}