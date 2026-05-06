package ru.practicum.sht.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.sht.broker.CollectorTopics;
import ru.practicum.sht.dto.hub.HubEvent;
import ru.practicum.sht.mapper.hub.HubEventMapper;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubEventServiceImpl implements HubEventService {
    //private final Producer<String, SpecificRecordBase> producer;
    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;
    private final HubEventMapper mapper;

    @Override
    public void sendHubEvent(HubEvent event) {
        HubEventAvro avroData = mapper.toAvro(event);
        String topic = CollectorTopics.TELEMETRY_HUBS_V1;
        String key = avroData.getHubId();

        //ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, key, avroData);
        log.info("Готовы данные в формате Avro: >>> {} <<< для отправки в Kafka-топик: >>> {} <<<", avroData, topic);

        //producer.send(record);
        kafkaTemplate.send(topic, key,avroData);
    }

}