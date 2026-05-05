package ru.practicum.sht.mapper.hub;

import ru.practicum.sht.dto.hub.scenario.ConditionType;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;

public class ConditionTypeMapper {

    public static ConditionTypeAvro toAvro(ConditionType type) {
        if (type == null) {
            return null;
        }

        return ConditionTypeAvro.valueOf(type.name());
    }

}