package ru.practicum.sht.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.NotNull;
//import lombok.RequiredArgsConstructor;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import ru.practicum.sht.dto.hub.HubEvent;
//import ru.practicum.sht.dto.sensor.SensorEvent;
import ru.practicum.sht.handler.hub.HubEventHandler;
import ru.practicum.sht.handler.sensor.SensorEventHandler;
//import ru.practicum.sht.service.HubEventService;
//import ru.practicum.sht.service.SensorEventService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc.CollectorControllerImplBase;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@GrpcService
//@RestController
//@RequestMapping("/events")
//@RequiredArgsConstructor
@Slf4j
@Validated
public class EventController extends CollectorControllerImplBase {
    //private final SensorEventService sensorEventService;
    //private final HubEventService hubEventService;
    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> sensorEventHandlers;
    private final Map<HubEventProto.PayloadCase, HubEventHandler> hubEventHandlers;

    public EventController(Set<SensorEventHandler> sensorEventHandlers, Set<HubEventHandler> hubEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(
                        SensorEventHandler::getMessageType,
                        Function.identity()
                ));

        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(
                        HubEventHandler::getMessageType,
                        Function.identity()
                ));
    }

    //@PostMapping("/sensors")
    /*public void collectSensorEvent(
                //@RequestBody
                @NotNull(message = "Данные о событии датчика не могут быть null")
                @Valid SensorEvent event
    ) {
        log.info("Поступили данные данные о новом событии датчика: {}.", event);
        sensorEventService.sendSensorEvent(event);
    }*/

    /**
     * Метод для обработки событий от датчиков.
     * Вызывается при получении нового события от gRPC-клиента.
     *
     * @param request           Событие от датчика
     * @param responseObserver  Ответ для клиента
     */
    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("Поступили данные данные о новом событии датчика: {}.", request);

        System.out.println("Поступили данные данные о новом событии датчика: " + request + " .");

        try {
            if (sensorEventHandlers.containsKey(request.getPayloadCase())) {
                sensorEventHandlers.get(request.getPayloadCase()).handle(request);
            } else {
                throw new IllegalArgumentException("Не могу найти обработчик для события " + request.getPayloadCase());
            }

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {


            System.err.println("!!! ERROR PROCESSING EVENT !!!");
            e.printStackTrace(System.err);


            /*responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));*/

            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage() != null ? e.getMessage() : e.toString())
                    .withCause(e)
                    .asRuntimeException());


        }
    }

    //@PostMapping("/hubs")
    /*public void collectHubEvent(
                //@RequestBody
                @NotNull(message = "Данные о событии хаба не могут быть null")
                @Valid HubEvent event
    ) {
        log.info("Поступили данные данные о новом событии хаба: {}.", event);
        hubEventService.sendHubEvent(event);
    }*/

    /**
     * Метод для обработки событий от хабов.
     * Вызывается при получении нового события от gRPC-клиента.
     *
     * @param request           Событие от хаба
     * @param responseObserver  Ответ для клиента
     */
    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        log.info("Поступили данные данные о новом событии хаба: {}.", request);

        System.out.println("Поступили данные данные о новом событии хаба: " + request + " .");

        try {
            if (hubEventHandlers.containsKey(request.getPayloadCase())) {
                hubEventHandlers.get(request.getPayloadCase()).handle(request);
            } else {
                throw new IllegalArgumentException("Не могу найти обработчик для события " + request.getPayloadCase());
            }

            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {

            System.err.println("!!! ERROR PROCESSING EVENT !!!");
            e.printStackTrace(System.err);

            /*responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL
                            .withDescription(e.getLocalizedMessage())
                            .withCause(e)
            ));*/

            responseObserver.onError(Status.INTERNAL
                    .withDescription(e.getMessage() != null ? e.getMessage() : e.toString())
                    .withCause(e)
                    .asRuntimeException());
        }


    }

    @PostConstruct
    public void init() {
        System.out.println("!!! EVENT_CONTROLLER SUCCESSFULLY INITIALIZED BY SPRING !!!");
    }

}