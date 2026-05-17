package ru.practicum.sht.handler.sensor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.practicum.sht.broker.CollectorTopics;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.grpc.telemetry.event.TemperatureSensorProto;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

import java.time.Instant;

@Component
@RequiredArgsConstructor
@Slf4j
public class TemperatureSensorEventHandler implements SensorEventHandler {
    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;

    @Override
    public SensorEventProto.PayloadCase getMessageType() {
        return SensorEventProto.PayloadCase.TEMPERATURE_SENSOR;
    }

    @Override
    public void handle(SensorEventProto event) {
        TemperatureSensorProto temperatureSensorEvent = event.getTemperatureSensor();
        Instant timestamp = Instant.ofEpochSecond(event.getTimestamp().getSeconds(), event.getTimestamp().getNanos());
        SensorEventAvro avroData = SensorEventAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(timestamp)
                .setPayload(
                        TemperatureSensorAvro.newBuilder()
                                .setTemperatureC(temperatureSensorEvent.getTemperatureC())
                                .setTemperatureF(temperatureSensorEvent.getTemperatureF())
                                .build()
                )
                .build();

        String topic = CollectorTopics.TELEMETRY_SENSORS_V1;
        String key = avroData.getHubId();
        log.info("Готовы данные в формате Avro: >>> {} <<< для отправки в Kafka-топик: >>> {} <<<", avroData, topic);

        kafkaTemplate.send(topic, key,avroData);
    }

}