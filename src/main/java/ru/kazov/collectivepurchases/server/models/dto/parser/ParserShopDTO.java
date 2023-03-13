package ru.kazov.collectivepurchases.server.models.dto.parser;

import lombok.Data;

@Data
public class ParserShopDTO {
    private Long id;
    private String name;
    private String picture;
    private String baseUrl;
    private boolean needLogin;
    private String login;
    private String password;
}
