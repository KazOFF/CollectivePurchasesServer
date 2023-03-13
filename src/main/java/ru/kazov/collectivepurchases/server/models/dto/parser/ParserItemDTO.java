package ru.kazov.collectivepurchases.server.models.dto.parser;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ParserItemDTO {
    private Long id;
    private Long categoryId;
    private String url;
    private String name;
    private String description;
    private List<String> pictures;
    private Map<String, Double> prices;
    private Map<String, String> properties;
}
