package org.micromall.catalog.handler;

import java.util.List;

import lombok.Builder;

@Builder
public record MyErrorResponse(
        String message,
        List<MyError> errors) {

}
