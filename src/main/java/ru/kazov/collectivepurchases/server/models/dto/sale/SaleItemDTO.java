package ru.kazov.collectivepurchases.server.models.dto.sale;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SaleItemDTO {
    private Long id;
    private String name;
    private String description;
    private String priceComment;
    private String url;
    private String vkPhotoUrl;
    private List<String> pictures;
    private Map<String, Double> prices;
    private Map<String, String> properties;
}
