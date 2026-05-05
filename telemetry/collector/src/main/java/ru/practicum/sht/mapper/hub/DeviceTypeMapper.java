package ru.practicum.sht.mapper.hub;

import ru.practicum.sht.dto.hub.device.DeviceType;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;

public class DeviceTypeMapper {

    public static DeviceTypeAvro toAvro(DeviceType type) {
        if (type == null) {
            return null;
        }

        return DeviceTypeAvro.valueOf(type.name());
    }

}