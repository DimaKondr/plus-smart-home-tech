package ru.practicum.sht.handler;

import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Optional;

public interface SnapshotHandler {

    Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event);

}