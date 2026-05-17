package ru.practicum.sht.mapper.hub;

import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;

public class DeviceActionMapper {

    /*public static DeviceActionAvro toAvro(DeviceAction action) {
        if (action == null) {
            return null;
        }

        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(ActionTypeMapper.toAvro(action.getType()))
                .setValue(action.getValue())
                .build();
    }*/

    public static DeviceActionAvro toAvro(DeviceActionProto action) {
        if (action == null) {
            return null;
        }

        Integer actionValue = action.hasValue() ? action.getValue() : null;

        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setType(ActionTypeMapper.toAvro(action.getType()))
                //.setValue(action.getValue())
                .setValue(actionValue)
                .build();
    }

}