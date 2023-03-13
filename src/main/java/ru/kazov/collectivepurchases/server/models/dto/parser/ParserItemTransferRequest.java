package ru.kazov.collectivepurchases.server.models.dto.parser;

import lombok.Data;

import java.util.List;

@Data
public class ParserItemTransferRequest {
    List<Long> parserItemList;
    Long saleCategoryId;
    Double rate;
    Double scale;
    String priceComment;
    int roundPlaces;
}
