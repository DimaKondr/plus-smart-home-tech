package ru.practicum.sht.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.sht.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.practicum.sht.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.practicum.sht.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class WarehouseErrorHandler {

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouseException.class)
    public ResponseEntity<Map<String, Object>> handleLowQuantityException(
            final ProductInShoppingCartLowQuantityInWarehouseException e
    ) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", e.getMessage());
        body.put("missingProducts", e.getMissingProductsErrors());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler({
            SpecifiedProductAlreadyInWarehouseException.class,
            NoSpecifiedProductInWarehouseException.class
    })
    public ResponseEntity<Map<String, Object>> handleWarehouseExceptions(Exception e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeExceptions(Exception e) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("error", e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}