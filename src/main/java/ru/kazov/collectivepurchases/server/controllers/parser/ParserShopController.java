package ru.kazov.collectivepurchases.server.controllers.parser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kazov.collectivepurchases.server.common.ObjectMapper;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserShop;
import ru.kazov.collectivepurchases.server.models.dto.parser.ParserShopDTO;
import ru.kazov.collectivepurchases.server.services.parser.ParserShopService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RequiredArgsConstructor
@Tag(name = "ParserShop", description = "Parser shop management API")
@RestController
@RequestMapping("parser/shop")
public class ParserShopController {
    private final ParserShopService parserShopService;
    private final ObjectMapper objectMapper;


    @Operation(summary = "Get all parser shops")
    @ApiResponse(responseCode = "200")
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<List<ParserShopDTO>> getAllParserShops() {
        List<ParserShop> parserShopDAOList =  parserShopService.getParserShops();
        List<ParserShopDTO> parserShopDTOList = objectMapper.convertParserShops(parserShopDAOList);
        return ResponseEntity.ok(parserShopDTOList);
    }


    @Operation(summary = "Create new parser shop")
    @ApiResponse(responseCode = "201")
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ParserShopDTO> createParserShop(@RequestBody ParserShopDTO parserShopDTO) throws URISyntaxException {
        ParserShop parserShopDAO = objectMapper.convertParserShop(parserShopDTO);
        Long id = parserShopService.createParserShop(parserShopDAO);
        ParserShopDTO persistParserShop = objectMapper.convertParserShop(parserShopDAO);
        return ResponseEntity.created(new URI("/parser/shop/" + id)).body(persistParserShop);
    }

    @Operation(summary = "Get parser shop")
    @ApiResponse(responseCode = "200")
    @GetMapping(value = "/{parserShopId}", produces = "application/json")
    public ResponseEntity<ParserShopDTO> getParserShop(@PathVariable Long parserShopId) {
        ParserShop parserShopDAO = parserShopService.getParserShop(parserShopId);
        ParserShopDTO parserShopDTO = objectMapper.convertParserShop(parserShopDAO);
        return ResponseEntity.ok(parserShopDTO);
    }

    @Operation(summary = "Update parser shop")
    @ApiResponse(responseCode = "200")
    @PutMapping(value = "/{parserShopId}", produces = "application/json")
    public ResponseEntity<ParserShopDTO> updateParserShop(@PathVariable Long parserShopId, @RequestBody ParserShopDTO parserShopDTO) {
        ParserShop parserShopDAO = objectMapper.convertParserShop(parserShopDTO);
        parserShopService.updateParserShop(parserShopDAO, parserShopId);
        ParserShopDTO persistParserShopDTO = objectMapper.convertParserShop(parserShopDAO);
        return ResponseEntity.ok(persistParserShopDTO);
    }

    @Operation(summary = "Delete parser shop")
    @ApiResponse(responseCode = "204")
    @DeleteMapping(value = "/{parserShopId}")
    public ResponseEntity<Void> deleteParserJob(@PathVariable Long parserShopId) {
        parserShopService.removeParserShop(parserShopId);
        return ResponseEntity.noContent().build();
    }
}
