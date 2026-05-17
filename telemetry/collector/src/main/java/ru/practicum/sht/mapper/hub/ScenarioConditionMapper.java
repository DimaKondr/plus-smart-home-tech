package ru.practicum.sht.mapper.hub;

import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

public class ScenarioConditionMapper {

    /*public static ScenarioConditionAvro toAvro(ScenarioCondition scenario) {
        if (scenario == null) {
            return null;
        }

        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenario.getSensorId())
                .setType(ConditionTypeMapper.toAvro(scenario.getType()))
                .setOpertaion(ConditionOperationMapper.toAvro(scenario.getOperation()))
                .setValue(scenario.getValue())
                .build();
    }*/

    public static ScenarioConditionAvro toAvro(ScenarioConditionProto scenario) {
        if (scenario == null) {
            return null;
        }

        return ScenarioConditionAvro.newBuilder()
                .setSensorId(scenario.getSensorId())
                .setType(ConditionTypeMapper.toAvro(scenario.getType()))
                .setOpertaion(ConditionOperationMapper.toAvro(scenario.getOperation()))
                .setValue(getProtoValue(scenario))
                .build();
    }

    private static Object getProtoValue(ScenarioConditionProto scenario) {
        Object protoValue = null;

        switch (scenario.getValueCase()) {
            case INT_VALUE:
                protoValue = scenario.getIntValue();
                break;
            case BOOL_VALUE:
                protoValue = scenario.getBoolValue();
                break;
            case VALUE_NOT_SET:
                protoValue = null;
                break;
        }

        return protoValue;
    }

}