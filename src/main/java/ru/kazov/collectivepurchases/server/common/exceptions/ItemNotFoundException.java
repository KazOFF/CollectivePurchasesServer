package ru.kazov.collectivepurchases.server.common.exceptions;

import org.springframework.http.HttpStatus;

public class ItemNotFoundException extends CPServerException {
    public ItemNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Item not found");
    }
}
