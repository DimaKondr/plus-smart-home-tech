package ru.practicum.sht.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.sht.exception.response.ShoppingCartLowQuantityErrorResponse;
import ru.practicum.sht.exception.shopping.cart.NoProductsInShoppingCartException;
import ru.practicum.sht.exception.shopping.cart.NotAuthorizedUserException;
import ru.practicum.sht.exception.warehouse.ProductInShoppingCartLowQuantityInWarehouseException;

@RestControllerAdvice
public class ShoppingCartErrorHandler {

    @ExceptionHandler(ProductInShoppingCartLowQuantityInWarehouseException.class)
    public ResponseEntity<ShoppingCartLowQuantityErrorResponse> handleLowQuantityException(
            ProductInShoppingCartLowQuantityInWarehouseException e
    ) {
        ShoppingCartLowQuantityErrorResponse body = new ShoppingCartLowQuantityErrorResponse(
                e.getMessage(),
                e.getMissingProductsErrors()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(NoProductsInShoppingCartException.class)
    public ResponseEntity<ErrorResponse> handleNoProductsInShoppingCartExceptions(Exception e) {
        ErrorResponse body = new ErrorResponse(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(NotAuthorizedUserException.class)
    public ResponseEntity<ErrorResponse> handleNotAuthorizedUserExceptionExceptions(Exception e) {
        ErrorResponse body = new ErrorResponse(e.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeExceptions(Exception e) {
        ErrorResponse body = new ErrorResponse(e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}