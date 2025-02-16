package org.micromall.order.exception;

import java.util.List;

import org.micromall.order.handler.MyError;
import org.micromall.order.handler.MyErrorResponse;

public class MyAlreadyExists extends RuntimeException {
    @SuppressWarnings("unused")
    private MyErrorResponse response;

    public MyAlreadyExists(String message) {
        super(message);
        this.response = MyErrorResponse.builder().message(message).build();
    }

    public MyAlreadyExists(String message, String field) {
        this.response = MyErrorResponse.builder()
                .errors(List.of(MyError.builder().field(field).message(message).build())).build();
    }

    //  getters and setters

    public MyErrorResponse getResponse() {
        return response;
    }

    public void setResponse(MyErrorResponse response) {
        this.response = response;
    }

}
