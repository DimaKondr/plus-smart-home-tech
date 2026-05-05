package ru.practicum.sht.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Service;
import ru.practicum.sht.broker.CollectorTopics;
import ru.practicum.sht.dto.sensor.SensorEvent;
import ru.practicum.sht.mapper.sensor.SensorEventMapper;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

import java.util.concurrent.Future;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensorEventServiceImpl implements SensorEventService {
    //private final Producer<Void, SpecificRecordBase> producer;
    private final Producer<String, SpecificRecordBase> producer;
    private final SensorEventMapper mapper;

    @Override
    public void sendSensorEvent(SensorEvent event) {
        SensorEventAvro avroData = mapper.toAvro(event);
        String topic = CollectorTopics.TELEMETRY_SENSORS_V1;
        String key = avroData.getHubId();
        log.info("Готовы данные в формате Avro: >>> {} <<< для отправки в Kafka-топик: >>> {} <<<", avroData, topic);
        //ProducerRecord<Void, SpecificRecordBase> record = new ProducerRecord<>(topic, avroData);
        ProducerRecord<String, SpecificRecordBase> record = new ProducerRecord<>(topic, key, avroData);

        Future<RecordMetadata> answer = producer.send(record);
        //log.info("Попробуем посмотреть Future-answer: >>> isDone - {}, metadata - {} <<<", answer.isDone(), answer.resultNow());
        try {
            // get() дождется завершения задачи
            log.info("Future-answer: metadata - {}", answer.get());
        } catch (Exception e) {
            log.error("Failed to get Future answer", e);
        }
        /*if (answer.isDone()) {
            switch (answer.state()) {
                case SUCCESS -> log.info("Future-answer: metadata - {}", answer.resultNow());
                case FAILED -> log.error("Future-answer failed with exception: ", answer.exceptionNow());
                case CANCELLED -> log.warn("Future-answer was cancelled");
                default -> log.info("Future-answer is in state: {}", answer.state());
            }
        } else {
            log.info("Future-answer is still running (isDone - false)");
        }*/
        if (answer.isDone()) {
            switch (answer.state()) {
                case SUCCESS -> {
                    RecordMetadata rm = answer.resultNow();
                    System.out.println();
                    log.info("============================ FUTURE INFO - START ======================================");
                    log.info("Future-answer SUCCESS: " +
                                    "topic - {}, " +
                                    "partition - {}, " +
                                    "offset - {}, " +
                                    "timestamp - {}, " +
                                    "key size - {} bytes, " +
                                    "value size - {} bytes",
                            rm.topic(),
                            rm.partition(),
                            rm.offset(),
                            rm.hasTimestamp() ? rm.timestamp() : "N/A",
                            rm.serializedKeySize(),
                            rm.serializedValueSize()
                    );
                    log.info("============================ FUTURE INFO - END ======================================");
                    System.out.println();
                }
                case FAILED -> log.error("ERROR!!! Future-answer failed with exception: ", answer.exceptionNow());
                case CANCELLED -> log.warn("WARNING!!! Future-answer was cancelled");
                default -> log.info("Future-answer is in state: {}", answer.state());
            }
        } else {
            log.info("Future-answer is still running (isDone - false)");
        }

    }

}