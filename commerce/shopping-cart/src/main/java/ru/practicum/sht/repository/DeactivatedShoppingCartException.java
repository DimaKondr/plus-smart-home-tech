package ru.practicum.sht.repository;

public class DeactivatedShoppingCartException extends RuntimeException {
    public DeactivatedShoppingCartException(String message) {
        super(message);
    }
}
