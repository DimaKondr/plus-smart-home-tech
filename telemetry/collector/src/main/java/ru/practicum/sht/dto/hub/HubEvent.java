package ru.practicum.sht.dto.hub;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ru.practicum.sht.dto.ErrorEventType;
import ru.practicum.sht.dto.hub.device.DeviceAddedEvent;
import ru.practicum.sht.dto.hub.device.DeviceRemovedEvent;
import ru.practicum.sht.dto.hub.scenario.ScenarioAddedEvent;
import ru.practicum.sht.dto.hub.scenario.ScenarioRemovedEvent;

import java.time.Instant;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = ErrorEventType.class
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DeviceAddedEvent.class, name = "DEVICE_ADDED"),
        @JsonSubTypes.Type(value = DeviceRemovedEvent.class, name = "DEVICE_REMOVED"),
        @JsonSubTypes.Type(value = ScenarioAddedEvent.class, name = "SCENARIO_ADDED"),
        @JsonSubTypes.Type(value = ScenarioRemovedEvent.class, name = "SCENARIO_REMOVED")
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@ToString
public abstract class HubEvent {

    @NotBlank(message = "ID хаба не может быть null или пустым.")
    private String hubId;
    private Instant timestamp = Instant.now();

    public abstract HubEventType getType();
}