package ru.practicum.sht.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.sht.dto.hub.HubEvent;
import ru.practicum.sht.dto.sensor.SensorEvent;
import ru.practicum.sht.service.HubEventService;
import ru.practicum.sht.service.SensorEventService;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventController {
    private final SensorEventService sensorEventService;
    private final HubEventService hubEventService;

    @PostMapping("/sensors")
    public void collectSensorEvent(
                @RequestBody
                @NotNull(message = "Данные о событии датчика не могут быть null")
                @Valid SensorEvent event
    ) {
        log.info("Поступили данные данные о новом событии датчика: {}.", event);
        sensorEventService.sendSensorEvent(event);
    }

    @PostMapping("/hubs")
    public void collectHubEvent(
                @RequestBody
                @NotNull(message = "Данные о событии хаба не могут быть null")
                @Valid HubEvent event
    ) {
        log.info("Поступили данные данные о новом событии хаба: {}.", event);
        hubEventService.sendHubEvent(event);
    }

}