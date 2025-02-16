package org.micromall.catalog.exception;

import java.util.List;

import org.micromall.catalog.handler.MyError;
import org.micromall.catalog.handler.MyErrorResponse;

public class MyNotFoundException extends RuntimeException {
    @SuppressWarnings("unused")
    private MyErrorResponse response;

    // Contructor to thorw exception with message
    public MyNotFoundException(String message) {
        super(message);
        this.response = MyErrorResponse.builder().message(message).build();
    }

    // Contructor to thorw exception with error fields
    public MyNotFoundException(String  message, String field) {
        List<MyError> errors = List.of(MyError.builder().field(field).message(message).build());
        this.response = MyErrorResponse.builder().errors(errors).build();
    }

    // Getters and Setters

    public MyErrorResponse getResponse() {
        return response;
    }

    public void setResponse(MyErrorResponse response) {
        this.response = response;
    }
    
}
