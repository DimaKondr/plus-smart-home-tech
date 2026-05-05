package ru.practicum.sht.mapper.sensor;

import org.springframework.stereotype.Component;
import ru.practicum.sht.dto.sensor.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.temporal.ChronoUnit;

@Component
public class SensorEventMapper {

    public SensorEventAvro toAvro(SensorEvent event) {
        if (event == null) {
            return null;
        }

        SensorEventAvro data = new SensorEventAvro();
        data.setId(event.getId());
        data.setHubId(event.getHubId());
        //data.setTimestamp(event.getTimestamp());
        data.setTimestamp(event.getTimestamp().truncatedTo(ChronoUnit.MILLIS));

        switch (event.getType()) {
            case SensorEventType.MOTION_SENSOR_EVENT:
                MotionSensorEvent motionSensorEvent = (MotionSensorEvent) event;
                data.setPayload(
                        MotionSensorAvro.newBuilder()
                                .setLinkQuality(motionSensorEvent.getLinkQuality())
                                .setMotion(motionSensorEvent.isMotion())
                                .setVoltage(motionSensorEvent.getVoltage())
                                .build()
                );
                break;

            case SensorEventType.TEMPERATURE_SENSOR_EVENT:
                TemperatureSensorEvent temperatureSensorEvent = (TemperatureSensorEvent) event;
                data.setPayload(
                        TemperatureSensorAvro.newBuilder()
                                .setTemperatureC(temperatureSensorEvent.getTemperatureC())
                                .setTemperatureF(temperatureSensorEvent.getTemperatureF())
                                .build()
                );
                break;

            case SensorEventType.LIGHT_SENSOR_EVENT:
                LightSensorEvent lightSensorEvent = (LightSensorEvent) event;
                data.setPayload(
                        LightSensorAvro.newBuilder()
                                .setLinkQuality(lightSensorEvent.getLinkQuality())
                                .setLuminosity(lightSensorEvent.getLuminosity())
                                .build()
                );
                break;

            case SensorEventType.CLIMATE_SENSOR_EVENT:
                ClimateSensorEvent climateSensorEvent = (ClimateSensorEvent) event;
                data.setPayload(
                        ClimateSensorAvro.newBuilder()
                                .setTemperatureC(climateSensorEvent.getTemperatureC())
                                .setHumidity(climateSensorEvent.getHumidity())
                                .setCo2Level(climateSensorEvent.getCo2Level())
                                .build()
                );
                break;

            case SensorEventType.SWITCH_SENSOR_EVENT:
                SwitchSensorEvent switchSensorEvent = (SwitchSensorEvent) event;
                data.setPayload(
                        SwitchSensorAvro.newBuilder()
                                .setState(switchSensorEvent.isState())
                                .build()
                );
                break;
        }

        return data;
    }

}