package ru.practicum.sht.handler;

public interface HubEventHandler<T> {

    Class<T> getPayloadType();

    void handle(String hubId, T payload);
}