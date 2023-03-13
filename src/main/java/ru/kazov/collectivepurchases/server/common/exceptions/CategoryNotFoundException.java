package ru.kazov.collectivepurchases.server.common.exceptions;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends CPServerException {
    public CategoryNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Category not found");
    }
}
