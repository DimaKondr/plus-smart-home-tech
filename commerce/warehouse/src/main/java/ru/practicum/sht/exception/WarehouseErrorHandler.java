package ru.practicum.sht.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.sht.exception.response.WarehouseLowQuantityErrorResponse;
import ru.practicum.sht.exception.warehouse.NoSpecifiedProductInWarehouseException;
import ru.practicum.sht.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouseException;
import ru.practicum.sht.exception.warehouse.SpecifiedProductAlreadyInWarehouseException;

@RestControllerAdvice
public class WarehouseErrorHandler {

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouseException.class)
    public ResponseEntity<WarehouseLowQuantityErrorResponse> handleLowQuantityException(
            final ProductInShoppingCartLowQuantityInWarehouseException e
    ) {
        WarehouseLowQuantityErrorResponse body = new WarehouseLowQuantityErrorResponse(
                e.getMessage(),
                e.getMissingProductsErrors()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler({
            SpecifiedProductAlreadyInWarehouseException.class,
            NoSpecifiedProductInWarehouseException.class
    })
    public ResponseEntity<ErrorResponse> handleWarehouseExceptions(Exception e) {
        ErrorResponse body = new ErrorResponse(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeExceptions(Exception e) {
        ErrorResponse body = new ErrorResponse(e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}