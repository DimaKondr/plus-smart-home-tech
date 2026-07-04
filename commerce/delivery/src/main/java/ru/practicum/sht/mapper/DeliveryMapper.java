package ru.practicum.sht.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.sht.dto.delivery.DeliveryDto;
import ru.practicum.sht.dto.warehouse.AddressDto;
import ru.practicum.sht.model.Delivery;

@Component
public class DeliveryMapper {

    public Delivery toNewEntity(DeliveryDto dto) {
        if (dto == null) return null;

        Delivery delivery = new Delivery();

        delivery.setDeliveryId(dto.getDeliveryId());
        delivery.setOrderId(dto.getOrderId());
        delivery.setState(dto.getDeliveryState());
        delivery.setDeliveryWeight(null);
        delivery.setDeliveryVolume(null);
        delivery.setFragile(null);
        delivery.setFromCountry(dto.getFromAddress().getCountry());
        delivery.setFromCity(dto.getFromAddress().getCity());
        delivery.setFromStreet(dto.getFromAddress().getStreet());
        delivery.setFromHouse(dto.getFromAddress().getHouse());
        delivery.setFromFlat(dto.getFromAddress().getFlat());
        delivery.setToCountry(dto.getToAddress().getCountry());
        delivery.setToCity(dto.getToAddress().getCity());
        delivery.setToStreet(dto.getToAddress().getStreet());
        delivery.setToHouse(dto.getToAddress().getHouse());
        delivery.setToFlat(dto.getToAddress().getFlat());

        return delivery;
    }

    public DeliveryDto toDto(Delivery entity) {
        if (entity == null) return null;

        AddressDto fromAddress = AddressDto.builder()
                .country(entity.getFromCountry())
                .city(entity.getFromCity())
                .street(entity.getFromStreet())
                .house(entity.getFromHouse())
                .flat(entity.getFromFlat())
                .build();

        AddressDto toAddress = AddressDto.builder()
                .country(entity.getToCountry())
                .city(entity.getToCity())
                .street(entity.getToStreet())
                .house(entity.getToHouse())
                .flat(entity.getToFlat())
                .build();

        return DeliveryDto.builder()
                .deliveryId(entity.getDeliveryId())
                .fromAddress(fromAddress)
                .toAddress(toAddress)
                .orderId(entity.getOrderId())
                .deliveryState(entity.getState())
                .build();
    }

}