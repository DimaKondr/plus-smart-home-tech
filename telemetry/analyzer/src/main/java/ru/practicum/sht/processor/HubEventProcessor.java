package ru.practicum.sht.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.practicum.sht.broker.AnalyzerTopics;
import ru.practicum.sht.config.HubEventConsumerConfig;
import ru.practicum.sht.mapper.ActionMapper;
import ru.practicum.sht.mapper.ConditionMapper;
import ru.practicum.sht.model.Scenario;
import ru.practicum.sht.model.Sensor;
import ru.practicum.sht.repository.ScenarioRepository;
import ru.practicum.sht.repository.SensorRepository;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class HubEventProcessor implements Runnable {
    private final HubEventConsumerConfig consumerConfig;
    private final SensorRepository sensorRepository;
    private final ScenarioRepository scenarioRepository;
    private final ConditionMapper conditionMapper;
    private final ActionMapper actionMapper;
    private KafkaConsumer<String, HubEventAvro> consumer;


    @Override
    public void run() {
        try {
            Properties properties = new Properties();
            properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                    consumerConfig.getBootstrapServers());
            properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                    consumerConfig.getHubEventConsumer().getKeyDeserializer());
            properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                    consumerConfig.getHubEventConsumer().getValueDeserializer());
            properties.put(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                    consumerConfig.getHubEventConsumer().getAutoOffsetReset());
            properties.put(ConsumerConfig.GROUP_ID_CONFIG,
                    consumerConfig.getHubEventConsumer().getGroupId());

            this.consumer = new KafkaConsumer<>(properties);

            Runtime.getRuntime().addShutdownHook(new Thread(consumer::wakeup));

            consumer.subscribe(List.of(AnalyzerTopics.TELEMETRY_HUBS_V1));

            while (true) {
                ConsumerRecords<String, HubEventAvro> records = consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    log.info("Поступили данные события хаба: {}", record.value());
                    handleEvent(record.value());
                }
            }

        } catch (WakeupException ignored) {
            log.info("Получен сигнал остановки (WakeupException)");
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от хаба", e);
        } finally {
            try {
                log.info("Фиксация смещений перед закрытием...");
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
            }
        }
    }

    private void handleEvent(HubEventAvro hubEvent) {
        String hubId = hubEvent.getHubId();
        switch (hubEvent.getPayload()) {
            case DeviceAddedEventAvro deviceAddedEvent -> handleEvent(hubId, deviceAddedEvent);
            case DeviceRemovedEventAvro deviceRemovedEvent -> handleEvent(hubId, deviceRemovedEvent);
            case ScenarioAddedEventAvro scenarioAddedEvent -> handleEvent(hubId, scenarioAddedEvent);
            case ScenarioRemovedEventAvro scenarioRemovedEvent -> handleEvent(hubId, scenarioRemovedEvent);
            default -> log.error("Получено событие хаба неизвестного типа: {}", hubEvent);

        }
    }

    private void handleEvent(String hubId, DeviceAddedEventAvro event) {
        if (sensorRepository.existsByIdInAndHubId(List.of(event.getId()), hubId)) {
            log.info("Попытка добавления нового устройства. " +
                    "Устройство с ID: {} уже зарегистрировано в хабе с ID: {}.", event.getId(), hubId);
            return;
        }

        Sensor sensor = new Sensor();
        sensor.setId(event.getId());
        sensor.setHubId(hubId);

        sensorRepository.save(sensor);
        log.info("В хабе с ID: {} зарегистрировано новое устройство с ID: {}.", hubId, event.getId());
    }

    private void handleEvent(String hubId, DeviceRemovedEventAvro event) {
        if (!sensorRepository.existsByIdInAndHubId(List.of(event.getId()), hubId)) {
            log.info("Удаление устройства. Устройство с ID: {} не найдено в хабе с ID: {}.", event.getId(), hubId);
            return;
        }

        sensorRepository.deleteById(event.getId());
        log.info("Удаление устройства с ID: {} в хабе с ID: {}.", event.getId(), hubId);
    }

    private void handleEvent(String hubId, ScenarioAddedEventAvro event) {
        Optional<Scenario> addedScenario = scenarioRepository.findByHubIdAndName(hubId, event.getName());
        if (addedScenario.isPresent()) {
            log.info("Попытка добавления нового сценария. " +
                    "Сценарий с названием: {} уже зарегистрирован в хабе с ID: {}.", event.getName(), hubId);
            return;
        }

        Scenario scenario = new Scenario();
        scenario.setId(null);
        scenario.setHubId(hubId);
        scenario.setName(event.getName());
        scenario.setConditions(
                event.getConditions().stream()
                        .collect(Collectors.toMap(
                                ScenarioConditionAvro::getSensorId,
                                conditionMapper::fromAvro
                                )
                        )
        );
        scenario.setActions(
                event.getActions().stream()
                        .collect(Collectors.toMap(
                                DeviceActionAvro::getSensorId,
                                actionMapper::fromAvro
                                )
                        )
        );

        scenarioRepository.save(scenario);
        log.info("В хабе с ID: {} зарегистрирован новый сценарий с названием: {}.", hubId, event.getName());
    }

    private void handleEvent(String hubId, ScenarioRemovedEventAvro event) {
        Optional<Scenario> removedScenario = scenarioRepository.findByHubIdAndName(hubId, event.getName());
        if (removedScenario.isEmpty()) {
            log.info("Удаление сценария. Сценарий с названием: {} не найден в хабе с ID: {}.", event.getName(), hubId);
            return;
        }

        scenarioRepository.deleteById(removedScenario.get().getId());
        log.info("Удаление сценария с названием: {} в хабе с ID: {}.", event.getName(), hubId);
    }

}