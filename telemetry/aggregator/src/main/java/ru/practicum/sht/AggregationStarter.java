package ru.practicum.sht;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.practicum.sht.broker.AggregatorTopics;
import ru.practicum.sht.config.AggregatorConsumerConfig;
import ru.practicum.sht.handler.SnapshotProcessor;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Component
@RequiredArgsConstructor
@Slf4j
public class AggregationStarter {

    // ... объявление полей и конструктора ...
    //private final Properties config;
    //private final ConsumerFactory<String, SensorEventAvro> consumerFactory;
    private final AggregatorConsumerConfig consumerConfig;
    private final SnapshotProcessor handler;
    private final KafkaTemplate<String, SpecificRecordBase> producer;
    //private KafkaConsumer<String, SpecificRecordBase> consumer;
    private KafkaConsumer<String, SensorEventAvro> consumer;



    /*public AggregationStarter(Properties config, KafkaTemplate<String, SpecificRecordBase> producer) {
        this.config = config;
        this.producer = producer;
    }*/

    /**
     * Метод для начала процесса агрегации данных.
     * Подписывается на топики для получения событий от датчиков,
     * формирует снимок их состояния и записывает в кафку.
     */
    //@KafkaListener(topics = "telemetry.sensors.v1")
    public void start() {
        try {

            // ... подготовка к обработке данных ...
            // ... например, подписка на топик ...
            Properties props = new Properties();
            props.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                    consumerConfig.getBootstrapServers());
            props.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                    consumerConfig.getConsumer().getKeyDeserializer());
            props.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                    consumerConfig.getConsumer().getValueDeserializer());
            props.put(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                    consumerConfig.getConsumer().getAutoOffsetReset());
            props.put(ConsumerConfig.GROUP_ID_CONFIG,
                    consumerConfig.getConsumer().getGroupId());
            //this.consumer = (KafkaConsumer<String, SensorEventAvro>) consumerFactory.createConsumer();
            this.consumer = new KafkaConsumer<>(props);
            //String topic = AggregatorTopics.TELEMETRY_SENSORS_V1;
            consumer.subscribe(List.of(AggregatorTopics.TELEMETRY_SENSORS_V1));

            // Цикл обработки событий
            while (true) {
                // ... реализация цикла опроса ...
                // ... и обработка полученных данных ...
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    log.info("Поступили данные события датчика: {}", record.value());
                    Optional<SensorsSnapshotAvro> result = handler.updateState(record.value());
                    if (result.isPresent()) {
                        SensorsSnapshotAvro snapshotAvro = result.get();
                        String key = snapshotAvro.getHubId();
                        log.info("Готовы данные снимка состояния датчиков (snapshot) в формате Avro:" +
                                        " >>> {} <<< для отправки в Kafka-топик: >>> {} <<<",
                                snapshotAvro, AggregatorTopics.TELEMETRY_SNAPSHOTS_V1);

                        producer.send(AggregatorTopics.TELEMETRY_SNAPSHOTS_V1, key, snapshotAvro);
                    }
                }
            }

        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер и продюсер в блоке finally
            log.info("Получен сигнал остановки (WakeupException)");
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {
            try {
                // Перед тем, как закрыть продюсер и консьюмер, нужно убедиться,
                // что все сообщения, лежащие в буффере, отправлены и
                // все оффсеты обработанных сообщений зафиксированы
                log.info("Сброс буферов и фиксация смещений перед закрытием...");

                // здесь нужно вызвать метод продюсера для сброса данных в буффере
                // 1. Сбрасываем данные из буфера продюсера в сеть
                if (producer != null) {
                    producer.flush();
                }

                // здесь нужно вызвать метод консьюмера для фиксации смещений
                // 2. Фиксируем смещения обработанных сообщений (синхронно)
                if (consumer != null) {
                    consumer.commitSync();
                }

            } catch (Exception e) {
                log.error("Ошибка при финальном сбросе данных или коммите оффсетов", e);
            } finally {
                if (consumer != null) {
                    log.info("Закрываем консьюмер");
                    consumer.close();
                }

                if (producer != null) {
                    log.info("Закрываем продюсер");
                    producer.destroy();
                }
            }
        }
    }

}