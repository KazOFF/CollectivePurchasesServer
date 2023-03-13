package ru.kazov.collectivepurchases.server.common.exceptions;

import org.springframework.http.HttpStatus;

public class SaleNotFoundException extends CPServerException {
    public SaleNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Sale not found");
    }
}
