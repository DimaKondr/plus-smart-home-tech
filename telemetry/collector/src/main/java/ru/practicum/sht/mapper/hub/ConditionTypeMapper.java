package ru.practicum.sht.mapper.hub;

import ru.yandex.practicum.grpc.telemetry.event.ConditionTypeProto;
import ru.yandex.practicum.kafka.telemetry.event.ConditionTypeAvro;

public class ConditionTypeMapper {

    public static ConditionTypeAvro toAvro(ConditionTypeProto type) {
        if (type == null) {
            return null;
        }

        return ConditionTypeAvro.valueOf(type.name());
    }

}