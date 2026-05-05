package ru.practicum.sht.dto.hub.scenario;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.sht.dto.hub.HubEvent;
import ru.practicum.sht.dto.hub.HubEventType;

@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioRemovedEvent extends HubEvent {

    @Size(min = 3, message = "Название добавленного сценария должно содержать не менее 3 символов.")
    private String name;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_REMOVED;
    }

}