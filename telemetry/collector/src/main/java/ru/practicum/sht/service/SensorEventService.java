package ru.practicum.sht.service;

import ru.practicum.sht.dto.sensor.SensorEvent;

public interface SensorEventService {

    void sendSensorEvent(SensorEvent event);

}