package ru.practicum.sht.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.sht.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouseException;

import java.io.InputStream;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WarehouseErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();
    private final ObjectMapper objectMapper; // Spring Boot внедрит настроенный Jackson

    @Override
    public Exception decode(String methodKey, Response response) {
        // Проверяем, что это ошибка 400 Bad Request
        if (response.status() == 400 && response.body() != null) {
            try (InputStream bodyIs = response.body().asInputStream()) {

                /*// Десериализуем JSON от склада напрямую в ваше общее исключение
                ProductInShoppingCartLowQuantityInWarehouseException exception =
                        objectMapper.readValue(bodyIs, ProductInShoppingCartLowQuantityInWarehouseException.class);

                log.warn("Склад вернул ошибку 400: {}", exception.getMessage());
                return exception; // Возвращаем его, Feign сам его выбросит*/

                // 1. Читаем JSON в плоскую структуру DTO
                WarehouseErrorResponse errorDto = objectMapper.readValue(bodyIs, WarehouseErrorResponse.class);

                log.warn("Склад вернул ошибку 400: {}.", errorDto.getError());

                // 2. Вручную создаем кастомное исключение через ваш конструктор
                return new ProductInShoppingCartLowQuantityInWarehouseException(
                        errorDto.getError(),
                        errorDto.getMissingProducts() != null ? errorDto.getMissingProducts() : Map.of()
                );

            } catch (Exception e) {
                log.error("Не удалось десериализовать ошибку от склада.", e);
            }
        }

        // Для всех остальных ошибок (500, 404 и т.д.) используем стандартное поведение Feign
        return defaultDecoder.decode(methodKey, response);
    }

}