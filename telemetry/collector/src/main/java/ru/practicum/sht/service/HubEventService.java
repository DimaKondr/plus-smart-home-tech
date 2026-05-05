package ru.practicum.sht.service;

import ru.practicum.sht.dto.hub.HubEvent;

public interface HubEventService {

    void sendHubEvent(HubEvent event);

}