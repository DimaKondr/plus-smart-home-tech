package ru.practicum.sht.mapper.hub;

import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.kafka.telemetry.event.ActionTypeAvro;

public class ActionTypeMapper {

    public static ActionTypeAvro toAvro(ActionTypeProto type) {
        if (type == null) {
            return null;
        }

        return ActionTypeAvro.valueOf(type.name());
    }

}