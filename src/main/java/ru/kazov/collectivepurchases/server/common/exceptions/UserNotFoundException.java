package ru.kazov.collectivepurchases.server.common.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends CPServerException {
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "User not found");
    }
}
