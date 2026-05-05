package ru.practicum.sht.mapper.hub;

import ru.practicum.sht.dto.hub.scenario.DeviceAction;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;

public class DeviceActionMapper {

    public static DeviceActionAvro toAvro(DeviceAction action) {
        if (action == null) {
            return null;
        }

        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(ActionTypeMapper.toAvro(action.getType()))
                .setValue(action.getValue())
                .build();
    }

}