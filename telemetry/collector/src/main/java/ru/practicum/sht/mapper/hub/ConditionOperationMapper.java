package ru.practicum.sht.mapper.hub;

import ru.yandex.practicum.grpc.telemetry.event.ConditionOperationProto;
import ru.yandex.practicum.kafka.telemetry.event.ConditionOperationAvro;

public class ConditionOperationMapper {

    public static ConditionOperationAvro toAvro(ConditionOperationProto type) {
        if (type == null) {
            return null;
        }

        return ConditionOperationAvro.valueOf(type.name());
    }

}