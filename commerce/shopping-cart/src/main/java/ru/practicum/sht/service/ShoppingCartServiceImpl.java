package ru.practicum.sht.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.sht.contract.warehouse.WarehouseOperations;
import ru.practicum.sht.dto.shopping.cart.ShoppingCartDto;
import ru.practicum.sht.exception.shopping.cart.NoProductsInShoppingCartException;
import ru.practicum.sht.exception.shopping.cart.NotAuthorizedUserException;
import ru.practicum.sht.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.practicum.sht.mapper.ShoppingCartMapper;
import ru.practicum.sht.model.ShoppingCart;
import ru.practicum.sht.repository.DeactivatedShoppingCartException;
import ru.practicum.sht.repository.ShoppingCartRepository;
import ru.practicum.sht.request.shopping.cart.ChangeProductQuantityRequest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    //private final WarehouseClient warehouseClient;
    private final WarehouseOperations warehouseClient;

    @Override
    @Transactional
    public ShoppingCartDto getShoppingCart(String username) throws NotAuthorizedUserException {
        if (username == null || username.trim().isEmpty()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым.");
        }

        ShoppingCart shoppingCart = shoppingCartRepository.findByUsername(username)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setUsername(username);
                    return shoppingCartRepository.save(newCart);
                });

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto putToShoppingCart(String username, Map<UUID, Long> products)
            throws NotAuthorizedUserException {

        if (username == null || username.trim().isEmpty()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым.");
        }

        ShoppingCart shoppingCart = shoppingCartRepository.findByUsername(username)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setUsername(username);
                    return shoppingCartRepository.save(newCart);
                });
        log.info("Предыдущий состав корзины пользователя {} >>> {}.", username, shoppingCart.getProducts());

        if (!shoppingCart.isActive()) {
            throw new DeactivatedShoppingCartException("Нельзя добавлять товары в деактивированную корзину");
        }

        shoppingCart.getProducts().clear();

        if (products != null) {
            products.forEach((productId, quantity) -> {
                if (productId != null && quantity != null && quantity > 0) {
                    shoppingCart.getProducts().put(productId, quantity);
                }
            });
        }

        // 2. ПРОВЕРКА НА СКЛАДЕ: Маппим текущее (еще не сохраненное) состояние корзины и отправляем в Feign
        ShoppingCartDto previewDto = shoppingCartMapper.toDto(shoppingCart);
        log.info("Запрос на проверку доступности товаров на складе для пользователя {}.", username);
        // Если товаров не хватает, склад должен выбросить ошибку (например, 400 Bad Request),
        // которая прервет транзакцию и не сохранит изменения.
        warehouseClient.checkProduct(previewDto);
        //checkProducts(previewDto);

        /*try {
            warehouseClient.checkProduct(previewDto);
        } catch (ProductInShoppingCartLowQuantityInWarehouseException e) {
            log.error("На складе (сервис Warehouse) нет товара в нужном количестве. Получена ошибка: {}, {}.",
                    e.getMessage(), e.getMissingProductsErrors());
            throw new ProductInShoppingCartLowQuantityInWarehouseException(
                    e.getMessage(),
                    e.getMissingProductsErrors()
            );
        } catch (Exception e) {
            log.error("Неудачная попытка проверки наличия товара на складе (сервис Warehouse). Ошибка: {}.",
                    e.getMessage());
            throw new RuntimeException("Неудачная попытка проверки наличия товара на складе в сервисе Warehouse. " +
                    "Проверьте сервис Warehouse.", e);
        }*/

        ShoppingCart updatedCart = shoppingCartRepository.save(shoppingCart);
        log.info("Обновлен состав корзины пользователя {} >>> {}.", username, updatedCart.getProducts());

        return shoppingCartMapper.toDto(updatedCart);
    }

    @Override
    @Transactional
    public void removeShoppingCart(String username) throws NotAuthorizedUserException {
        if (username == null || username.trim().isEmpty()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым.");
        }

        ShoppingCart shoppingCart = shoppingCartRepository.findByUsername(username)
                .orElseThrow(() -> new NotAuthorizedUserException("Корзина для пользователя "
                        + username + " не найдена."));

        shoppingCart.setActive(false);
        shoppingCartRepository.save(shoppingCart);
        log.info("Корзина: {} у пользователя {} успешно деактивирована.", shoppingCart, username);
    }

    @Override
    @Transactional
    public ShoppingCartDto removeFromShoppingCart(String username, List<UUID> productIds)
            throws NoProductsInShoppingCartException, NotAuthorizedUserException {

        if (username == null || username.trim().isEmpty()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым.");
        }

        ShoppingCart shoppingCart = shoppingCartRepository.findByUsername(username)
                .orElseThrow(() -> new NotAuthorizedUserException("Корзина для пользователя "
                        + username + " не найдена."));

        if (!shoppingCart.isActive()) {
            throw new DeactivatedShoppingCartException("Нельзя изменять деактивированную корзину.");
        }

        if (productIds == null || productIds.isEmpty()) {
            throw new NoProductsInShoppingCartException("Список идентификаторов товаров для удаления пуст");
        }

        log.info("Процесс удаления товара из корзины. " +
                "Состояние корзины пользователя {} на текущий момент: {}.", username, shoppingCart);

        productIds.forEach(productId -> shoppingCart.getProducts().remove(productId));

        ShoppingCart updatedCart = shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(updatedCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto changeQuantity(String username, ChangeProductQuantityRequest request)
            throws NoProductsInShoppingCartException, NotAuthorizedUserException {

        if (username == null || username.trim().isEmpty()) {
            throw new NotAuthorizedUserException("Имя пользователя не должно быть пустым.");
        }

        ShoppingCart shoppingCart = shoppingCartRepository.findByUsername(username)
                .orElseThrow(() -> new NotAuthorizedUserException("Корзина для пользователя "
                        + username + " не найдена."));

        if (!shoppingCart.isActive()) {
            throw new DeactivatedShoppingCartException("Нельзя изменять деактивированную корзину.");
        }

        UUID productId = request.getProductId();

        if (!shoppingCart.getProducts().containsKey(productId)) {
            throw new NoProductsInShoppingCartException("Товар с ID: " + productId + " отсутствует в корзине.");
        }

        log.info("Процесс изменения количества товара в корзине. " +
                "Состояние корзины пользователя {} на текущий момент: {}.", username, shoppingCart);

        if (request.getNewQuantity() == 0) {
            shoppingCart.getProducts().remove(productId);
        } else {
            shoppingCart.getProducts().put(productId, request.getNewQuantity());
        }

        // 3. ПРОВЕРКА НА СКЛАДЕ: При изменении количества (в большую сторону) также валидируем остатки
        ShoppingCartDto previewDto = shoppingCartMapper.toDto(shoppingCart);
        log.info("Запрос на проверку доступности товаров при изменении количества для пользователя {}.", username);
        warehouseClient.checkProduct(previewDto);
        //checkProducts(previewDto);

        ShoppingCart updatedCart = shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(updatedCart);
    }

    /*private void checkProducts(ShoppingCartDto previewDto) {
        try {
            warehouseClient.checkProduct(previewDto);
        } catch (ProductInShoppingCartLowQuantityInWarehouseException e) {
            log.error("На складе (сервис Warehouse) нет товара в нужном количестве. Получена ошибка: {}, {}.",
                    e.getMessage(), e.getMissingProductsErrors());
            throw new ProductInShoppingCartLowQuantityInWarehouseException(
                    e.getMessage(),
                    e.getMissingProductsErrors()
            );
        } catch (Exception e) {
            log.error("Неудачная попытка проверки наличия товара на складе (сервис Warehouse). Ошибка: {}.",
                    e.getMessage());
            throw new RuntimeException("Неудачная попытка проверки наличия товара на складе в сервисе Warehouse. " +
                    "Проверьте сервис Warehouse.", e);
        }
    }*/

}