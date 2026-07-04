package ru.practicum.sht.exception.order;

public class NoOrderFoundException extends RuntimeException {
    public NoOrderFoundException(String message) {
        super(message);
    }
}