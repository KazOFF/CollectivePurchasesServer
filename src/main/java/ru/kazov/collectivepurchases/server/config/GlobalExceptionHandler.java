package ru.kazov.collectivepurchases.server.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.kazov.collectivepurchases.server.common.exceptions.CPServerException;
import ru.kazov.collectivepurchases.server.models.dto.ErrorResponse;

import java.util.Locale;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CPServerException.class})
    protected ResponseEntity<ErrorResponse> handelCPServerException(CPServerException exception, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage(), request.getRequestURL().toString()), exception.getHttpStatus());
    }


    protected ResponseEntity<ErrorResponse> handelGlobalException(Throwable exception, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse(request.getRequestURL().toString(), exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {SecurityException.class})
    public ResponseEntity<?> handleException(Throwable exception, WebRequest webRequest, Locale locale) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


}