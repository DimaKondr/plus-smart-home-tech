package ru.practicum.sht.dto.hub.scenario;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.sht.dto.hub.HubEvent;
import ru.practicum.sht.dto.hub.HubEventType;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioAddedEvent extends HubEvent {

    @Size(min = 3, message = "Название добавленного сценария должно содержать не менее 3 символов.")
    private String name;

    @NotEmpty(message = "Список условий, связанных со сценарием, не может быть null или пустым.")
    private List<ScenarioCondition> conditions;

    @NotEmpty(message = "Список действий, выполняемых в рамках сценария, не может быть null или пустым.")
    private List<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }

}