package ru.practicum.sht.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.sht.contract.delivery.DeliveryOperations;
import ru.practicum.sht.contract.payment.PaymentOperations;
import ru.practicum.sht.contract.warehouse.WarehouseOperations;
import ru.practicum.sht.dto.delivery.DeliveryDto;
import ru.practicum.sht.dto.delivery.DeliveryState;
import ru.practicum.sht.dto.order.OrderDto;
import ru.practicum.sht.dto.order.OrderState;
import ru.practicum.sht.dto.payment.PaymentDto;
import ru.practicum.sht.dto.warehouse.AddressDto;
import ru.practicum.sht.dto.warehouse.BookedProductsDto;
import ru.practicum.sht.exception.order.NoOrderFoundException;
import ru.practicum.sht.mapper.OrderMapper;
import ru.practicum.sht.model.Order;
import ru.practicum.sht.repository.OrderRepository;
import ru.practicum.sht.request.order.CreateNewOrderRequest;
import ru.practicum.sht.request.order.ProductReturnRequest;

import ru.practicum.sht.request.warehouse.AssemblyProductsForOrderRequest;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final WarehouseOperations warehouseClient;
    private final DeliveryOperations deliveryClient;
    private final PaymentOperations paymentClient;

    @Override
    public List<OrderDto> getClientOrders(String username) {
        return orderRepository.findAllByUsername(username).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDto createNewOrder(CreateNewOrderRequest request) {
        Order order = orderMapper.toNewOrderEntity(request);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderDto productReturn(ProductReturnRequest request) {
        Order order = getOrderOrThrowException(request.getOrderId());

        Map<UUID, Long> returnedProducts = request.getProducts();

        warehouseClient.returnProduct(returnedProducts);

        order.setState(OrderState.PRODUCT_RETURNED);
        Order returnedOrder = orderRepository.save(order);
        return orderMapper.toDto(returnedOrder);
    }

    @Override
    @Transactional
    public OrderDto payment(UUID orderId) {
        Order order = getOrderOrThrowException(orderId);

        OrderDto orderDto = orderMapper.toDto(order);
        PaymentDto paymentDto = paymentClient.payment(orderDto);

        order.setPaymentId(paymentDto.getPaymentId());
        order.setState(OrderState.PAID);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderDto paymentFailed(UUID orderId) {
        Order order = getOrderOrThrowException(orderId);
        order.setState(OrderState.PAYMENT_FAILED);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderDto delivery(UUID orderId) {
        Order order = getOrderOrThrowException(orderId);

        UUID newId = UUID.randomUUID();
        log.info("Сгенерирован новый UUID: {} для присвоения в качестве ID в новую доставку.", newId);

        AddressDto fromAddress = warehouseClient.getWarehouseAddress();
        log.info("Получен адрес склада, с которого будет осуществлена отправка новой доставки. " +
                "Адрес склада: {}.", fromAddress);

        AddressDto toAddress = AddressDto.builder()
                .country(order.getCountry())
                .city(order.getCity())
                .street(order.getStreet())
                .house(order.getHouse())
                .flat(order.getFlat())
                .build();

        DeliveryDto requestForNewDelivery = DeliveryDto.builder()
                .deliveryId(newId)
                .fromAddress(fromAddress)
                .toAddress(toAddress)
                .orderId(orderId)
                .deliveryState(DeliveryState.CREATED)
                .build();

        deliveryClient.planDelivery(requestForNewDelivery);

        order.setState(OrderState.ON_DELIVERY);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderDto deliveryFailed(UUID orderId) {
        Order order = getOrderOrThrowException(orderId);
        order.setState(OrderState.DELIVERY_FAILED);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderDto complete(UUID orderId) {
        Order order = getOrderOrThrowException(orderId);
        order.setState(OrderState.COMPLETED);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderDto calculateTotalCost(UUID orderId) {
        Order order = getOrderOrThrowException(orderId);
        OrderDto orderDto = orderMapper.toDto(order);

        BigDecimal productCost = paymentClient.productCost(orderDto);
        BigDecimal totalCost = paymentClient.getTotalCost(orderDto);

        order.setProductPrice(productCost);
        order.setTotalPrice(totalCost);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderDto calculateDeliveryCost(UUID orderId) {
        Order order = getOrderOrThrowException(orderId);
        OrderDto orderDto = orderMapper.toDto(order);

        BigDecimal deliveryCost = deliveryClient.deliveryCost(orderDto);

        order.setDeliveryPrice(deliveryCost);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional
    public OrderDto assembly(UUID orderId) {
        Order order = getOrderOrThrowException(orderId);

        AssemblyProductsForOrderRequest request = AssemblyProductsForOrderRequest.builder()
                .orderId(orderId)
                .products(order.getProducts())
                .build();

        BookedProductsDto bookedProductsDto = warehouseClient
                .assemblyProductForOrderFromShoppingCart(request, order.getDeliveryId());

        order.setDeliveryWeight(bookedProductsDto.getDeliveryWeight());
        order.setDeliveryVolume(bookedProductsDto.getDeliveryVolume());
        order.setFragile(bookedProductsDto.getFragile());
        order.setState(OrderState.ASSEMBLED);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    @Transactional
    public OrderDto assemblyFailed(UUID orderId) {
        Order order = getOrderOrThrowException(orderId);
        order.setState(OrderState.ASSEMBLY_FAILED);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    private Order getOrderOrThrowException(UUID orderId) {
        Optional<Order> order = orderRepository.findById(orderId);

        if (order.isEmpty()) {
            log.error("Заказ c ID: {} не найден.", orderId);
            throw new NoOrderFoundException("Заказ не найден.");
        }

        return order.get();
    }

}