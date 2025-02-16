package org.micromall.catalog.exception;

import java.util.List;

import org.micromall.catalog.handler.MyError;
import org.micromall.catalog.handler.MyErrorResponse;

public class MyNotDeleteException extends RuntimeException {

    @SuppressWarnings("unused")
    private MyErrorResponse response;

    public MyNotDeleteException(String message) {
        super(message);
        this.response = MyErrorResponse.builder().message(message).build();
    }

    public MyNotDeleteException(String message, String field) {
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
