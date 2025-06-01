package org.micromall.userapi.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Custom exception for API related errors
 */
@Getter
public class ApiException extends RuntimeException {
    private final HttpStatus status;
    private final String message;
    private final String code;

    public ApiException(String message, HttpStatus status, String code) {
        super(message);
        this.message = message;
        this.status = status;
        this.code = code;
    }

    public ApiException(String message, HttpStatus status) {
        this(message, status, status.name());
    }
}
