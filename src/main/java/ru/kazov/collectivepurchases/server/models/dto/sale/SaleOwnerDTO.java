package ru.kazov.collectivepurchases.server.models.dto.sale;

import lombok.Data;

@Data
public class SaleOwnerDTO {
    private Long id;
    private String fullname;
    private String email;
    private String vkId;
    private String telegramId;
}
