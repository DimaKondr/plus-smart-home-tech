package ru.practicum.sht.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.sht.exception.order.NoOrderFoundException;
import ru.practicum.sht.exception.shopping.cart.NotAuthorizedUserException;
import ru.practicum.sht.exception.warehouse.NoSpecifiedProductInWarehouseException;

@RestControllerAdvice
public class OrderErrorHandler {

    @ExceptionHandler(NoOrderFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoOrderFoundExceptions(Exception e) {
        ErrorResponse body = new ErrorResponse(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(NoSpecifiedProductInWarehouseException.class)
    public ResponseEntity<ErrorResponse> handleNoSpecifiedProductInWarehouseExceptions(Exception e) {
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