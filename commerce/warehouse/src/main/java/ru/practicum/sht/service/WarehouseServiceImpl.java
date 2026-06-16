package ru.practicum.sht.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.sht.contract.shopping.store.ShoppingStoreOperations;
import ru.practicum.sht.dto.shopping.cart.ShoppingCartDto;
import ru.practicum.sht.dto.shopping.store.QuantityState;
import ru.practicum.sht.dto.warehouse.AddressDto;
import ru.practicum.sht.dto.warehouse.BookedProductsDto;
import ru.practicum.sht.exception.shopping.store.ProductNotFoundException;
import ru.practicum.sht.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.practicum.sht.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.practicum.sht.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;
import ru.practicum.sht.mapper.ProductMapper;
import ru.practicum.sht.model.WarehouseProduct;
import ru.practicum.sht.model.WarehouseStock;
import ru.practicum.sht.repository.WarehouseProductRepository;
import ru.practicum.sht.repository.WarehouseStockRepository;
import ru.practicum.sht.request.shopping.store.SetProductQuantityStateRequest;
import ru.practicum.sht.request.warehouse.AddProductToWarehouseRequest;
import ru.practicum.sht.request.warehouse.NewProductInWarehouseRequest;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseServiceImpl implements WarehouseService {
    private static final String[] ADDRESSES = new String[] {"ADDRESS_1", "ADDRESS_2"};
    private static final String CURRENT_ADDRESS =
            ADDRESSES[Random.from(new SecureRandom()).nextInt(0, ADDRESSES.length)];


    private final WarehouseProductRepository productRepository;
    private final WarehouseStockRepository stockRepository;
    private final ProductMapper productMapper;
    private final ShoppingStoreOperations shoppingStoreClient;

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
    public BookedProductsDto checkProduct(ShoppingCartDto dto)
            throws ProductInShoppingCartLowQuantityInWarehouseException {

        double totalWeight = 0.0;
        double totalVolume = 0.0;
        boolean isFragile = false;

        Map<UUID, String> missingProductsErrors = new HashMap<>();

        for (Map.Entry<UUID, Long> entry : dto.getProducts().entrySet()) {
            UUID productId = entry.getKey();
            long requestedQuantity = entry.getValue();

            WarehouseProduct product = productRepository.findById(productId).orElse(null);
            if (product == null) {
                missingProductsErrors.put(productId, "Неизвестный товар. " +
                        "Данный тип товара на складе ранее не регистрировался.");
                continue;
            }

            long availableQuantity = stockRepository.findById(productId)
                    .map(WarehouseStock::getQuantity)
                    .orElse(0L);

            if (availableQuantity < requestedQuantity) {
                missingProductsErrors.put(productId, String.format("Недостаточное количество на складе. " +
                                "Запрошено: %d >>> Доступно: %d.", requestedQuantity, availableQuantity));
            }

            if (!missingProductsErrors.isEmpty()) {
                continue;
            }

            totalWeight += product.getWeight() * requestedQuantity;

            double singleVolume = product.getWidth() * product.getHeight() * product.getDepth();
            totalVolume += singleVolume * requestedQuantity;

            if (product.getFragile()) {
                isFragile = true;
            }
        }

        if (!missingProductsErrors.isEmpty()) {
            throw new ProductInShoppingCartLowQuantityInWarehouseException(
                    "Некоторые товары отсутствуют в требуемом количестве.", missingProductsErrors
            );
        }

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

        //oldQuantity.setQuantity(oldQuantity.getQuantity() + request.getQuantity());
        Long updatedQuantity = currentQuantity + request.getQuantity();
        WarehouseStock updatedStock = new WarehouseStock(productId, updatedQuantity);

        WarehouseStock newQuantity = stockRepository.save(updatedStock);
        log.info("После приемки на складе стало {} единиц товара с ID: {}.",
                newQuantity.getQuantity(), newQuantity.getProductId());

        /*SetProductQuantityStateRequest quantityStateRequest = SetProductQuantityStateRequest.builder()
                .productId(newQuantity.getProductId())
                .quantityState(setupQuantityState(newQuantity.getQuantity()))
                .build();

        System.out.println("ПОПЫТКА ОТПРАВИТЬ ДАННЫЕ В SHOPPINGSTORE: " + quantityStateRequest);

        try {
            Boolean hasQuantityStateUpdate = shoppingStoreClient.setQuantityState(quantityStateRequest);
            if (hasQuantityStateUpdate) {
                log.info("В сервис ShoppingStore для товара с ID: {} отправлен статус количества: {}.",
                        quantityStateRequest.getProductId(), quantityStateRequest.getQuantityState());
                log.info("Обновление данных о количестве в сервисе ShoppingStore успешно завершено.");
            } else {
                log.error("В сервис ShoppingStore для товара с ID: {} отправлен статус количества: {}.",
                        quantityStateRequest.getProductId(), quantityStateRequest.getQuantityState());
                log.error("Обновление данных о количестве в сервисе ShoppingStore НЕ ОСУЩЕСТВЛЕНО!");
            }
        } catch (Exception e) {
            log.error("Неудачная попытка отправки данных о количестве товара в сервис ShoppingStore. Данные: {}.",
                    quantityStateRequest);

            System.out.println(e.getMessage());

            throw new RuntimeException("Неудачная попытка отправки данных о количестве товара " +
                    "в сервис ShoppingStore. Проверьте сервис ShoppingStore.", e);
        }*/
    }

    @Override
    public AddressDto getAddress() {
        return AddressDto.builder()
                .country(CURRENT_ADDRESS)
                .city(CURRENT_ADDRESS)
                .street(CURRENT_ADDRESS)
                .house(CURRENT_ADDRESS)
                .flat(CURRENT_ADDRESS)
                .build();
    }

    private QuantityState setupQuantityState(Long quantity) {
        if (quantity > 0 && quantity < 10) {
            return QuantityState.FEW;
        } else if (quantity >= 10 && quantity <= 100) {
            return QuantityState.ENOUGH;
        } else if (quantity > 100) {
            return QuantityState.MANY;
        } else {
            return QuantityState.ENDED;
        }
    }

}