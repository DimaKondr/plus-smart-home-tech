package ru.practicum.sht.mapper.hub;

import ru.yandex.practicum.grpc.telemetry.event.DeviceTypeProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

public class DeviceTypeMapper {

    /*public static DeviceTypeAvro toAvro(DeviceType type) {
        if (type == null) {
            return null;
        }

        return DeviceTypeAvro.valueOf(type.name());
    }*/

    public static DeviceTypeAvro toAvro(DeviceTypeProto type) {
        if (type == null) {
            return null;
        }

        return DeviceTypeAvro.valueOf(type.name());
    }

}