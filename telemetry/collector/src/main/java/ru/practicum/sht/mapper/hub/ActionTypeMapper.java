package ru.practicum.sht.mapper.hub;

import ru.practicum.sht.dto.hub.scenario.DeviceActionType;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;

public class ActionTypeMapper {

    public static ActionTypeAvro toAvro(DeviceActionType type) {
        if (type == null) {
            return null;
        }

        return ActionTypeAvro.valueOf(type.name());
    }

}