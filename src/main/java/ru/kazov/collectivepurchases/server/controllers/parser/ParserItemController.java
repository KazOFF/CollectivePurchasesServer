package ru.kazov.collectivepurchases.server.controllers.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kazov.collectivepurchases.server.common.ObjectMapper;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserItem;
import ru.kazov.collectivepurchases.server.models.dto.parser.ParserItemDTO;
import ru.kazov.collectivepurchases.server.models.dto.parser.ParserItemTransferRequest;
import ru.kazov.collectivepurchases.server.services.parser.ParserItemService;

import java.util.List;

@RequiredArgsConstructor
@Tag(name = "ParserItem", description = "Parser item management API")
@RestController
@RequestMapping("/parser/catalog/{parserCategoryId}/items")
public class ParserItemController {
    private final ParserItemService parserItemService;
    private final ObjectMapper objectMapper;

    @Operation(summary = "Get all parser item")
    @ApiResponse(responseCode = "200")
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<List<ParserItemDTO>> getAllParserItems(@PathVariable Long parserCategoryId) {
        List<ParserItem> parserItemDAOList = parserItemService.getParserItems(parserCategoryId);
        List<ParserItemDTO> parserItemDTOList = objectMapper.convertParserItems(parserItemDAOList);
        return ResponseEntity.ok(parserItemDTOList);
    }

    @Operation(summary = "Get parser item")
    @ApiResponse(responseCode = "200")
    @GetMapping(value = "/{parserItemId}", produces = "application/json")
    public ResponseEntity<ParserItemDTO> getParserItem(@PathVariable Long parserCategoryId, @PathVariable Long parserItemId) {
        ParserItem parserItemDAO = parserItemService.getParserItem(parserItemId);
        ParserItemDTO parserItemDTO = objectMapper.convertParserItem(parserItemDAO);
        return ResponseEntity.ok(parserItemDTO);
    }

    @Operation(summary = "Delete parser item")
    @ApiResponse(responseCode = "204")
    @DeleteMapping(value = "/{parserItemId}")
    public ResponseEntity<Void> deleteParserItem(@PathVariable Long parserCategoryId, @PathVariable Long parserItemId) {
        parserItemService.removeParserItem(parserItemId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Batch delete parser item")
    @ApiResponse(responseCode = "204")
    @DeleteMapping(value = "/batch", consumes = "application/json")
    public ResponseEntity<Void> batchDeleteParserItem(@PathVariable Long parserCategoryId, @RequestBody List<Long> ids) {
        parserItemService.batchRemoveParserItems(ids);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Transfer parser items to sale")
    @ApiResponse(responseCode = "204")
    @PostMapping(value = "/transfer", consumes =  "application/json")
    public ResponseEntity<Void> transferToSale(@PathVariable Long parserCategoryId, @RequestBody ParserItemTransferRequest transferRequest) {
        parserItemService.transferToSale(
                transferRequest.getParserItemList(),
                transferRequest.getSaleCategoryId(),
                transferRequest.getRate(),
                transferRequest.getScale(),
                transferRequest.getPriceComment(),
                transferRequest.getRoundPlaces());
        return ResponseEntity.noContent().build();
    }

}
