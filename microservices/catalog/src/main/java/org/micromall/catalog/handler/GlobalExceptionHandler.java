package org.micromall.catalog.handler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.micromall.catalog.exception.MyNotDeleteException;
import org.micromall.catalog.exception.MyNotFoundException;
import org.micromall.catalog.exception.MyNotSaveException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<MyErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<MyError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::mapFieldError)
                .collect(Collectors.toList());

        MyErrorResponse errorResponse = MyErrorResponse.builder()
                .errors(errors)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Helper method to map field errors
    private MyError mapFieldError(FieldError fieldError) {
        return MyError.builder()
                .field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .build();
    }

    // Handle MyNotSaveException
    @ExceptionHandler(MyNotSaveException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<MyErrorResponse> handleMyNotSaveException(MyNotSaveException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getResponse());
    }

    // Handle MyNotFoundException
    @ExceptionHandler(MyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<MyErrorResponse> handleMyNotFoundException(MyNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getResponse());
    }

    // Handle MyNotDeleteException
    @ExceptionHandler(MyNotDeleteException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<MyErrorResponse> handleMyNotDeleteException(MyNotDeleteException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getResponse());
    }
}
