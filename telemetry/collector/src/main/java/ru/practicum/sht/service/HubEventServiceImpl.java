package ru.practicum.sht.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
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
    private final KafkaTemplate<String, SpecificRecordBase> kafkaTemplate;
    private final HubEventMapper mapper;

    @Override
    public void sendHubEvent(HubEvent event) {
        HubEventAvro avroData = mapper.toAvro(event);
        String topic = CollectorTopics.TELEMETRY_HUBS_V1;
        String key = avroData.getHubId();
        log.info("Готовы данные в формате Avro: >>> {} <<< для отправки в Kafka-топик: >>> {} <<<", avroData, topic);

        kafkaTemplate.send(topic, key, avroData);
    }

}