package ru.kazov.collectivepurchases.server.models.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema
public class ErrorResponse {

    private final String message;
    private final String url;

}
