package ru.kazov.collectivepurchases.server.common.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidDataException extends CPServerException {
    public InvalidDataException() {
        super(HttpStatus.BAD_REQUEST, "Invalid data");
    }
}
