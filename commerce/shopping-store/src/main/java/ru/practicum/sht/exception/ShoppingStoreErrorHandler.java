package ru.practicum.sht.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.sht.exception.response.ShoppingStoreProductNotFoundByIdListErrorResponse;
import ru.practicum.sht.exception.shopping.store.ProductNotFoundByIdListException;
import ru.practicum.sht.exception.shopping.store.ProductNotFoundException;

@RestControllerAdvice
public class ShoppingStoreErrorHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundExceptions(Exception e) {
        ErrorResponse body = new ErrorResponse(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(ProductNotFoundByIdListException.class)
    public ResponseEntity<ShoppingStoreProductNotFoundByIdListErrorResponse> handleProductNotFoundByIdListException(
            final ProductNotFoundByIdListException e
    ) {
        ShoppingStoreProductNotFoundByIdListErrorResponse body = new ShoppingStoreProductNotFoundByIdListErrorResponse(
                e.getMessage(),
                e.getMissingProductsIdList()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeExceptions(Exception e) {
        ErrorResponse body = new ErrorResponse(e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}