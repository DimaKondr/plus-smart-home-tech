package ru.practicum.sht.mapper.hub;

import org.springframework.stereotype.Component;
import ru.practicum.sht.dto.hub.HubEvent;
import ru.practicum.sht.dto.hub.HubEventType;
import ru.practicum.sht.dto.hub.device.DeviceRemovedEvent;
import ru.practicum.sht.dto.hub.scenario.ScenarioRemovedEvent;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.temporal.ChronoUnit;

@Component
public class HubEventMapper {

    public HubEventAvro toAvro(HubEvent event) {
        if (event == null) {
            return null;
        }

        HubEventAvro data = new HubEventAvro();
        data.setHubId(event.getHubId());
        data.setTimestamp(event.getTimestamp().truncatedTo(ChronoUnit.MILLIS));

        switch (event.getType()) {
            /*case HubEventType.DEVICE_ADDED:
                DeviceAddedEvent deviceAddedEvent = (DeviceAddedEvent) event;
                data.setPayload(
                        DeviceAddedEventAvro.newBuilder()
                                .setId(deviceAddedEvent.getId())
                                .setType(DeviceTypeMapper.toAvro(deviceAddedEvent.getDeviceType()))
                                .build()
                );
                break;*/

            case HubEventType.DEVICE_REMOVED:
                DeviceRemovedEvent deviceRemovedEvent = (DeviceRemovedEvent) event;
                data.setPayload(
                        DeviceRemovedEventAvro.newBuilder()
                                .setId(deviceRemovedEvent.getId())
                                .build()
                );
                break;

            /*case HubEventType.SCENARIO_ADDED:
                ScenarioAddedEvent scenarioAddedEvent = (ScenarioAddedEvent) event;
                data.setPayload(
                        ScenarioAddedEventAvro.newBuilder()
                                .setName(scenarioAddedEvent.getName())
                                .setConditions(
                                        scenarioAddedEvent.getConditions().stream()
                                                .map(ScenarioConditionMapper::toAvro)
                                                .toList()
                                )
                                .setActions(
                                        scenarioAddedEvent.getActions().stream()
                                                .map(DeviceActionMapper::toAvro)
                                                .toList()
                                )
                                .build()
                );
                break;*/

            case HubEventType.SCENARIO_REMOVED:
                ScenarioRemovedEvent scenarioRemovedEvent = (ScenarioRemovedEvent) event;
                data.setPayload(
                        ScenarioRemovedEventAvro.newBuilder()
                                .setName(scenarioRemovedEvent.getName())
                                .build()
                );
                break;
        }

        return data;
    }

}