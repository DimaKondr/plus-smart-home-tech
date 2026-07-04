package ru.practicum.sht.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.sht.dto.shopping.cart.ShoppingCartDto;
import ru.practicum.sht.dto.warehouse.AddressDto;
import ru.practicum.sht.dto.warehouse.BookedProductsDto;
import ru.practicum.sht.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.practicum.sht.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.practicum.sht.exception.warehouse.ProductInShoppingCartNotInWarehouse;
import ru.practicum.sht.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;
import ru.practicum.sht.mapper.ProductMapper;
import ru.practicum.sht.model.OrderAssembly;
import ru.practicum.sht.model.WarehouseStock;
import ru.practicum.sht.repository.ProductWithStockShort;
import ru.practicum.sht.repository.WarehouseOrderAssemblyRepository;
import ru.practicum.sht.repository.WarehouseProductRepository;
import ru.practicum.sht.repository.WarehouseStockRepository;
import ru.practicum.sht.request.warehouse.AddProductToWarehouseRequest;
import ru.practicum.sht.request.warehouse.AssemblyProductsForOrderRequest;
import ru.practicum.sht.request.warehouse.NewProductInWarehouseRequest;
import ru.practicum.sht.request.warehouse.ShippedToDeliveryRequest;

import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {
    private static final String[] ADDRESSES = new String[] {"ADDRESS_1", "ADDRESS_2"};
    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];

    private final WarehouseProductRepository productRepository;
    private final WarehouseStockRepository stockRepository;
    private final WarehouseOrderAssemblyRepository orderAssemblyRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public void addNewProduct(NewProductInWarehouseRequest request)
            throws SpecifiedProductAlreadyInWarehouseException {

        if (productRepository.existsById(request.getProductId())) {
            throw new SpecifiedProductAlreadyInWarehouseException(
                    "Товар с ID: " + request.getProductId() + " уже зарегистрирован на складе.");
        }

        productRepository.save(productMapper.toEntity(request));
    }

    @Override
    @Transactional
    public void shipProduct(ShippedToDeliveryRequest request) {
        OrderAssembly orderAssembly = new OrderAssembly();

        orderAssembly.setOrderId(request.getOrderId());
        orderAssembly.setDeliveryId(request.getDeliveryId());

        orderAssemblyRepository.save(orderAssembly);
    }

    @Override
    @Transactional
    public void returnProduct(Map<UUID, Long> returnItems) {
        if (returnItems == null || returnItems.isEmpty()) {
            log.error("Передан пустой перечень возвращаемых товаров или перечень равен null: {}", returnItems);
            return;
        }

        List<WarehouseStock> stocksToIncrement = stockRepository.findAllById(returnItems.keySet());

        for (WarehouseStock stock : stocksToIncrement) {
            Long quantityToAdd = returnItems.get(stock.getProductId());
            stock.setQuantity(stock.getQuantity() + quantityToAdd);
        }

        stockRepository.saveAll(stocksToIncrement);
    }

    @Override
    public BookedProductsDto checkProduct(ShoppingCartDto dto)
            throws ProductInShoppingCartNotInWarehouse {

        Map<UUID, Long> requestedProducts = dto.getProducts();
        Set<UUID> productIds = requestedProducts.keySet();

        Map<UUID, ProductWithStockShort> productsMap = productRepository
                .findAllProductsWithStock(productIds)
                .stream()
                .collect(Collectors.toMap(ProductWithStockShort::getProductId, Function.identity()));

        Map<UUID, String> missingProductsErrors = new HashMap<>();
        double totalWeight = 0.0;
        double totalVolume = 0.0;
        boolean isFragile = false;

        for (Map.Entry<UUID, Long> entry : requestedProducts.entrySet()) {
            UUID productId = entry.getKey();
            long requestedQuantity = entry.getValue();

            ProductWithStockShort product = productsMap.get(productId);
            if (product == null) {
                missingProductsErrors.put(productId, "Неизвестный товар. " +
                        "Данный тип товара на складе ранее не регистрировался.");
                continue;
            }

            long availableQuantity = product.getQuantity() != null ? product.getQuantity() : 0L;
            if (availableQuantity < requestedQuantity) {
                missingProductsErrors.put(productId, String.format("Недостаточное количество на складе. " +
                        "Запрошено: %d >>> Доступно: %d.", requestedQuantity, availableQuantity));
            }
        }

        if (!missingProductsErrors.isEmpty()) {
            throw new ProductInShoppingCartNotInWarehouse(
                    "Некоторые товары отсутствуют в требуемом количестве.", missingProductsErrors
            );
        }

        for (Map.Entry<UUID, Long> entry : requestedProducts.entrySet()) {
            UUID productId = entry.getKey();
            long requestedQuantity = entry.getValue();

            ProductWithStockShort product = productsMap.get(productId);

            totalWeight += product.getWeight() * requestedQuantity;

            double singleVolume = product.getWidth() * product.getHeight() * product.getDepth();
            totalVolume += singleVolume * requestedQuantity;

            if (Boolean.TRUE.equals(product.getFragile())) {
                isFragile = true;
            }
        }

        return BookedProductsDto.builder()
                .deliveryWeight(totalWeight)
                .deliveryVolume(totalVolume)
                .fragile(isFragile)
                .build();
    }

    @Override
    @Transactional
    public BookedProductsDto assemblyProductForOrderFromShoppingCart(
            AssemblyProductsForOrderRequest request,
            UUID deliveryId
    ) throws ProductInShoppingCartLowQuantityInWarehouseException {

        Map<UUID, Long> requestedProducts = request.getProducts();
        Set<UUID> productIds = requestedProducts.keySet();

        Map<UUID, ProductWithStockShort> productsMap = productRepository
                .findAllProductsWithStock(productIds)
                .stream()
                .collect(Collectors.toMap(ProductWithStockShort::getProductId, Function.identity()));

        Map<UUID, String> missingProductsErrors = new HashMap<>();
        double totalWeight = 0.0;
        double totalVolume = 0.0;
        boolean isFragile = false;

        for (Map.Entry<UUID, Long> entry : requestedProducts.entrySet()) {
            UUID productId = entry.getKey();
            long requestedQuantity = entry.getValue();

            ProductWithStockShort product = productsMap.get(productId);
            if (product == null) {
                missingProductsErrors.put(productId, "Неизвестный товар. " +
                        "Данный тип товара на складе ранее не регистрировался.");
                continue;
            }

            long availableQuantity = product.getQuantity() != null ? product.getQuantity() : 0L;
            if (availableQuantity < requestedQuantity) {
                missingProductsErrors.put(productId, String.format("Недостаточное количество на складе. " +
                        "Запрошено: %d >>> Доступно: %d.", requestedQuantity, availableQuantity));
            }
        }

        if (!missingProductsErrors.isEmpty()) {
            throw new ProductInShoppingCartLowQuantityInWarehouseException(
                    "Некоторые товары отсутствуют в требуемом количестве.", missingProductsErrors
            );
        }

        List<WarehouseStock> stocksToUpdate = new ArrayList<>();

        for (Map.Entry<UUID, Long> entry : requestedProducts.entrySet()) {
            UUID productId = entry.getKey();
            long requestedQuantity = entry.getValue();

            ProductWithStockShort product = productsMap.get(productId);

            totalWeight += product.getWeight() * requestedQuantity;
            double singleVolume = product.getWidth() * product.getHeight() * product.getDepth();
            totalVolume += singleVolume * requestedQuantity;

            if (Boolean.TRUE.equals(product.getFragile())) {
                isFragile = true;
            }

            WarehouseStock stock = new WarehouseStock();
            stock.setProductId(productId);

            long availableQuantity = product.getQuantity() != null ? product.getQuantity() : 0L;
            stock.setQuantity(availableQuantity - requestedQuantity);

            stocksToUpdate.add(stock);
        }

        stockRepository.saveAll(stocksToUpdate);

        OrderAssembly orderAssembly = new OrderAssembly(request.getOrderId(), deliveryId);
        orderAssemblyRepository.save(orderAssembly);

        return BookedProductsDto.builder()
                .deliveryWeight(totalWeight)
                .deliveryVolume(totalVolume)
                .fragile(isFragile)
                .build();
    }

    @Override
    @Transactional
    public void acceptProductToWarehouse(AddProductToWarehouseRequest request)
            throws NoSpecifiedProductInWarehouseException {

        if (!productRepository.existsById(request.getProductId())) {
            throw new NoSpecifiedProductInWarehouseException(
                    "Информации о товаре с ID: " + request.getProductId() + " на складе отсутствует."
            );
        }

        WarehouseStock oldStock = stockRepository.findById(request.getProductId())
                .orElseGet(() -> new WarehouseStock(request.getProductId(), 0L));
        Long currentQuantity = oldStock.getQuantity();
        UUID productId = oldStock.getProductId();
        log.info("На текущий момент на складе {} единиц товара с ID: {}.",
                currentQuantity, productId);

        Long updatedQuantity = currentQuantity + request.getQuantity();
        WarehouseStock updatedStock = new WarehouseStock(productId, updatedQuantity);

        WarehouseStock newQuantity = stockRepository.save(updatedStock);
        log.info("После приемки на складе стало {} единиц товара с ID: {}.",
                newQuantity.getQuantity(), newQuantity.getProductId());
    }

    @Override
    public AddressDto getWarehouseAddress() {
        return AddressDto.builder()
                .country(CURRENT_ADDRESS)
                .city(CURRENT_ADDRESS)
                .street(CURRENT_ADDRESS)
                .house(CURRENT_ADDRESS)
                .flat(CURRENT_ADDRESS)
                .build();
    }

}