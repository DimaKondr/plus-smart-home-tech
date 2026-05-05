package ru.practicum.sht.mapper.hub;

public class ScenarioAddedEventMapper {

    /*public static ScenarioAddedEventAvro toAvro(ScenarioAddedEvent event) {
        if (event == null) {
            return null;
        }

        *//*List<ScenarioAddedEventAvro> result = new ArrayList<>();
        for (ScenarioAddedEvent event : events) {
            result.add(
                    ScenarioAddedEventAvro.newBuilder()
                            .setName(event.getName())
                            .setConditions(
                                    event.getConditions().stream()
                                            .map(ScenarioConditionMapper::toAvro)
                                            .toList()
                            )
                            .setActions(
                                    event.getActions().stream()
                                            .map(DeviceActionMapper::toAvro)
                                            .toList()
                            )
                            .build()
            );
        }*//*

        return ScenarioAddedEventAvro.newBuilder()
                .setName(event.getName())
                .setConditions(
                        event.getConditions().stream()
                                .map(ScenarioConditionMapper::toAvro)
                                .toList()
                )
                .setActions(
                        event.getActions().stream()
                                .map(DeviceActionMapper::toAvro)
                                .toList()
                )
                .build();
    }*/

}