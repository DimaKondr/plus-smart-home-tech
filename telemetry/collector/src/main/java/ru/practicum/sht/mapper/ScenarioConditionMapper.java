package ru.practicum.sht.mapper;

import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

public class ScenarioConditionMapper {

    public static ScenarioConditionAvro toAvro(ScenarioConditionProto scenario) {
        if (scenario == null) {
            return null;
        }

        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenario.getSensorId())
                .setType(ConditionTypeMapper.toAvro(scenario.getType()))
                .setOperation(ConditionOperationMapper.toAvro(scenario.getOperation()))
                .setValue(getProtoValue(scenario))
                .build();
    }

    private static Object getProtoValue(ScenarioConditionProto scenario) {
        if (scenario.hasIntValue() && scenario.getValueCase() == ScenarioConditionProto.ValueCase.INT_VALUE) {
            return scenario.getIntValue();
        }

        if (scenario.hasBoolValue() && scenario.getValueCase() == ScenarioConditionProto.ValueCase.BOOL_VALUE) {
            return scenario.getBoolValue();
        }

        return null;
    }

}