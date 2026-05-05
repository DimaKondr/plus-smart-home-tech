package ru.practicum.sht.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Service;
import ru.practicum.sht.broker.CollectorTopics;
import ru.practicum.sht.dto.sensor.SensorEvent;
import ru.practicum.sht.mapper.sensor.SensorEventMapper;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorEventServiceImpl implements SensorEventService {
    private final Producer<String, SpecificRecordBase> producer;
    private final SensorEventMapper mapper;

    @Override
    public void sendSensorEvent(SensorEvent event) {
        SensorEventAvro avroData = mapper.toAvro(event);
        String topic = CollectorTopics.TELEMETRY_SENSORS_V1;
        String key = avroData.getHubId();

        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, key, avroData);
        log.info("Готовы данные в формате Avro: >>> {} <<< для отправки в Kafka-топик: >>> {} <<<", avroData, topic);

        producer.send(record);
    }

}