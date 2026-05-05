package ru.practicum.sht.dto.hub.device;

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
public class DeviceRemovedEvent extends HubEvent {
    private String id;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }

}