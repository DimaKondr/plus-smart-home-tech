package ru.practicum.sht.service;

import ru.practicum.sht.dto.sensor.SensorEvent;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

public interface SensorEventService {

    void sendSensorEvent(SensorEvent event);

    //void sendSensorEvent(SensorEventProto event);

}