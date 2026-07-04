package ru.practicum.sht.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.sht.dto.order.OrderDto;
import ru.practicum.sht.dto.order.OrderState;
import ru.practicum.sht.model.Order;
import ru.practicum.sht.request.order.CreateNewOrderRequest;

@Component
public class OrderMapper {

    public OrderDto toDto(Order entity) {
        return OrderDto.builder()
                .orderId(entity.getOrderId())
                .shoppingCartId(entity.getShoppingCartId())
                .products(entity.getProducts())
                .paymentId(entity.getPaymentId())
                .deliveryId(entity.getDeliveryId())
                .state(entity.getState())
                .deliveryWeight(entity.getDeliveryWeight())
                .deliveryVolume(entity.getDeliveryVolume())
                .fragile(entity.getFragile())
                .totalPrice(entity.getTotalPrice())
                .deliveryPrice(entity.getDeliveryPrice())
                .productPrice(entity.getProductPrice())
                .build();
    }

    public Order toNewOrderEntity(CreateNewOrderRequest request) {
        Order entity = new Order();

        entity.setShoppingCartId(request.getShoppingCart().getShoppingCartId());
        entity.setProducts(request.getShoppingCart().getProducts());
        entity.setPaymentId(null);
        entity.setDeliveryId(null);
        entity.setState(OrderState.NEW);
        entity.setDeliveryWeight(null);
        entity.setDeliveryVolume(null);
        entity.setFragile(null);
        entity.setTotalPrice(null);
        entity.setProductPrice(null);
        entity.setDeliveryPrice(null);
        // TODO: Доработать setUsername() когда будет ясно как в OrderController будете передаваться username.
        entity.setUsername(null);
        entity.setCountry(request.getDeliveryAddress().getCountry());
        entity.setCity(request.getDeliveryAddress().getCity());
        entity.setStreet(request.getDeliveryAddress().getStreet());
        entity.setHouse(request.getDeliveryAddress().getHouse());
        entity.setFlat(request.getDeliveryAddress().getFlat());

        return entity;
    }

}