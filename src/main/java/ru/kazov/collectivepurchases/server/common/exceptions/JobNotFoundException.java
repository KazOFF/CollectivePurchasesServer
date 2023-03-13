package ru.kazov.collectivepurchases.server.common.exceptions;

import org.springframework.http.HttpStatus;

public class JobNotFoundException extends CPServerException {
    public JobNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Job not found");
    }
}
