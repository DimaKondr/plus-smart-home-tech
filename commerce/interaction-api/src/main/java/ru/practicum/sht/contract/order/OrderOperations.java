package ru.practicum.sht.contract.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.practicum.sht.request.order.CreateNewOrderRequest;
import ru.practicum.sht.dto.order.OrderDto;
import ru.practicum.sht.request.order.ProductReturnRequest;
import ru.practicum.sht.exception.order.NoOrderFoundException;
import ru.practicum.sht.exception.shopping.cart.NotAuthorizedUserException;
import ru.practicum.sht.exception.warehouse.NoSpecifiedProductInWarehouseException;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/order")
public interface OrderOperations {

    @GetMapping
    List<OrderDto> getClientOrders(
            @RequestParam
                @NotBlank(message = "Имя пользователя не может быть null и пустым")
                String username
    ) throws NotAuthorizedUserException;

    @PutMapping
    OrderDto createNewOrder(
            @RequestBody
                @Valid CreateNewOrderRequest request,
            @RequestParam
                @NotBlank(message = "Имя пользователя не может быть null и пустым")
                String username
    ) throws NoSpecifiedProductInWarehouseException;

    @PostMapping("/return")
    OrderDto productReturn(@RequestBody @Valid ProductReturnRequest request)
            throws NoOrderFoundException;

    @PostMapping("/payment")
    OrderDto payment(@RequestBody UUID orderId) throws NoOrderFoundException;

    @PostMapping("/payment/failed")
    OrderDto paymentFailed(@RequestBody UUID orderId) throws NoOrderFoundException;

    @PostMapping("/delivery")
    OrderDto delivery(@RequestBody UUID orderId) throws NoOrderFoundException;

    @PostMapping("/delivery/failed")
    OrderDto deliveryFailed(@RequestBody UUID orderId) throws NoOrderFoundException;

    @PostMapping("/completed")
    OrderDto complete(@RequestBody UUID orderId) throws NoOrderFoundException;

    @PostMapping("/calculate/total")
    OrderDto calculateTotalCost(@RequestBody UUID orderId) throws NoOrderFoundException;

    @PostMapping("/calculate/delivery")
    OrderDto calculateDeliveryCost(@RequestBody UUID orderId) throws NoOrderFoundException;

    @PostMapping("/assembly")
    OrderDto assembly(@RequestBody UUID orderId) throws NoOrderFoundException;

    @PostMapping("/assembly/failed")
    OrderDto assemblyFailed(@RequestBody UUID orderId) throws NoOrderFoundException;

}