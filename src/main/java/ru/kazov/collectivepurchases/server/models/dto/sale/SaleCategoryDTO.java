package ru.kazov.collectivepurchases.server.models.dto.sale;


import lombok.Data;


@Data
public class SaleCategoryDTO {
    private Long id;
    private Long saleId;
    private String name;
    private String picture;
}
