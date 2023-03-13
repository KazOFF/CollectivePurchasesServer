package ru.kazov.collectivepurchases.server.models.dto.parser;

import lombok.Data;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserJobStatus;

@Data
public class ParserJobDTO {
    private Long id;
    private Long parserShopId;
    private ParserJobStatus status;
    private String url;
    private String message;
}
