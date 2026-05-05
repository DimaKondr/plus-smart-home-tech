package ru.practicum.sht.mapper.hub;

import ru.practicum.sht.dto.hub.scenario.ScenarioCondition;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

public class ScenarioConditionMapper {

    public static ScenarioConditionAvro toAvro(ScenarioCondition scenario) {
        if (scenario == null) {
            return null;
        }

        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenario.getSensorId())
                .setType(ConditionTypeMapper.toAvro(scenario.getType()))
                .setOpertaion(ConditionOperationMapper.toAvro(scenario.getOperation()))
                .setValue(scenario.getValue())
                .build();
    }

}