package ru.practicum.sht.mapper.hub;

import ru.practicum.sht.dto.hub.scenario.ConditionOperation;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;

public class ConditionOperationMapper {

    public static ConditionOperationAvro toAvro(ConditionOperation type) {
        if (type == null) {
            return null;
        }

        return ConditionOperationAvro.valueOf(type.name());
    }

}