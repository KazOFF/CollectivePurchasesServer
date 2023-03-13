package ru.kazov.collectivepurchases.server.common.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CPServerException extends RuntimeException {
    private final HttpStatus httpStatus;

    public CPServerException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

}
