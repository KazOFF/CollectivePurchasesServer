package ru.kazov.collectivepurchases.server.common.exceptions;

import org.springframework.http.HttpStatus;

public class JwtAuthException extends CPServerException {
    public JwtAuthException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

    public JwtAuthException() {
        super(HttpStatus.UNAUTHORIZED, "Jwt Authentication problem");
    }
}
