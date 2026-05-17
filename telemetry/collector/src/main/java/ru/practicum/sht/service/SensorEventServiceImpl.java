package ru.practicum.sht.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.sht.broker.CollectorTopics;
import ru.practicum.sht.dto.sensor.SensorEvent;
import ru.practicum.sht.mapper.sensor.SensorEventMapper;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorEventServiceImpl implements SensorEventService {
    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;
    private final SensorEventMapper mapper;

    @Override
    public void sendSensorEvent(SensorEvent event) {
        SensorEventAvro avroData = mapper.toAvro(event);
        String topic = CollectorTopics.TELEMETRY_SENSORS_V1;
        String key = avroData.getHubId();
        log.info("Готовы данные в формате Avro: >>> {} <<< для отправки в Kafka-топик: >>> {} <<<", avroData, topic);

        kafkaTemplate.send(topic, key, avroData);
    }

}