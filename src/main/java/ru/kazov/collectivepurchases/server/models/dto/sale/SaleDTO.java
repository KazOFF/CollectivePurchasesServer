package ru.kazov.collectivepurchases.server.models.dto.sale;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.kazov.collectivepurchases.server.models.dao.sale.SaleCountry;

import java.util.Date;

@Data
public class SaleDTO {
    private Long id;
    private Long ownerId;
    private String name;
    private String picture;
    private SaleCountry country;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date endDate;
    private boolean active;
}
