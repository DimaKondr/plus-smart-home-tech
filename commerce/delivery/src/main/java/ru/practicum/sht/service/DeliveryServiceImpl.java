package ru.practicum.sht.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.sht.contract.order.OrderOperations;
import ru.practicum.sht.contract.warehouse.WarehouseOperations;
import ru.practicum.sht.dto.delivery.DeliveryDto;
import ru.practicum.sht.dto.delivery.DeliveryState;
import ru.practicum.sht.dto.order.OrderDto;
import ru.practicum.sht.dto.warehouse.AddressDto;
import ru.practicum.sht.exception.delivery.NoDeliveryFoundException;
import ru.practicum.sht.mapper.DeliveryMapper;
import ru.practicum.sht.model.Delivery;
import ru.practicum.sht.repository.DeliveryRepository;
import ru.practicum.sht.request.warehouse.ShippedToDeliveryRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final OrderOperations orderClient;
    private final WarehouseOperations warehouseClient;

    private static final BigDecimal BASE_COST = new BigDecimal("5.0");


    @Override
    @Transactional
    public DeliveryDto planDelivery(DeliveryDto request) {
        Delivery delivery = deliveryMapper.toNewEntity(request);

        Delivery savedDelivery = deliveryRepository.save(delivery);
        DeliveryDto dto = deliveryMapper.toDto(savedDelivery);

        log.info("Запланирована отгрузка: {}", dto);
        return dto;
    }

    @Override
    @Transactional
    public void markAsSuccessful(UUID orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException("Доставка для заказа " + orderId + " не найдена."));

        delivery.setState(DeliveryState.DELIVERED);
        deliveryRepository.save(delivery);

        orderClient.complete(orderId);
        log.info("Доставка заказа c ID: {} успешно завершена.", orderId);
    }

    @Override
    @Transactional
    public void markAsPicked(UUID orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException("Доставка для заказа " + orderId + " не найдена."));

        delivery.setState(DeliveryState.IN_PROGRESS);
        deliveryRepository.save(delivery);

        orderClient.assembly(orderId);

        ShippedToDeliveryRequest warehouseRequest = ShippedToDeliveryRequest.builder()
                .orderId(delivery.getOrderId())
                .deliveryId(delivery.getDeliveryId())
                .build();

        warehouseClient.shippedToDelivery(warehouseRequest);
        log.info("На склад отправлен запрос на доставку: {}", warehouseRequest);
    }

    @Override
    @Transactional
    public void markAsFailed(UUID orderId) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NoDeliveryFoundException("Доставка для заказа " + orderId + " не найдена."));

        delivery.setState(DeliveryState.FAILED);
        deliveryRepository.save(delivery);

        orderClient.deliveryFailed(orderId);
        log.info("Неудачная попытка доставки заказа с ID: {}.", orderId);
    }

    @Override
    public BigDecimal deliveryCost(OrderDto order) {
        AddressDto warehouseAddress = warehouseClient.getWarehouseAddress();

        Delivery delivery = deliveryRepository.findByOrderId(order.getOrderId())
                .orElseThrow(() -> new NoDeliveryFoundException("Доставка для заказа "
                        + order.getOrderId() + " не найдена."));

        AddressDto customerAddress = AddressDto.builder()
                .country(delivery.getToCountry())
                .city(delivery.getToCity())
                .street(delivery.getToStreet())
                .house(delivery.getToHouse())
                .flat(delivery.getToFlat())
                .build();

        BigDecimal currentSum = BASE_COST;

        if ("ADDRESS_2".equals(warehouseAddress.getCountry())) {
            BigDecimal warehouseFactor = BASE_COST.multiply(new BigDecimal("2"));
            currentSum = currentSum.add(warehouseFactor);
        } else if ("ADDRESS_1".equals(warehouseAddress.getCountry())) {
            BigDecimal warehouseFactor = BASE_COST.multiply(new BigDecimal("1"));
            currentSum = currentSum.add(warehouseFactor);
        }

        if (Boolean.TRUE.equals(delivery.getFragile())) {
            BigDecimal fragileFactor = currentSum.multiply(new BigDecimal("0.2"));
            currentSum = currentSum.add(fragileFactor);
        }

        if (delivery.getDeliveryWeight() != null) {
            BigDecimal weightValue = BigDecimal.valueOf(delivery.getDeliveryWeight());
            BigDecimal weightFactor = weightValue.multiply(new BigDecimal("0.3"));
            currentSum = currentSum.add(weightFactor);
        }

        if (delivery.getDeliveryVolume() != null) {
            BigDecimal volumeValue = BigDecimal.valueOf(delivery.getDeliveryVolume());
            BigDecimal volumeFactor = volumeValue.multiply(new BigDecimal("0.2"));
            currentSum = currentSum.add(volumeFactor);
        }

        if (!warehouseAddress.getStreet().equals(customerAddress.getStreet())) {
            BigDecimal addressFactor = currentSum.multiply(new BigDecimal("0.2"));
            currentSum = currentSum.add(addressFactor);
        }

        return currentSum.setScale(2, RoundingMode.HALF_UP);
    }

}