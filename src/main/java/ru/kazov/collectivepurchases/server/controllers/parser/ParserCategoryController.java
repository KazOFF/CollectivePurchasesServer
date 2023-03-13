package ru.kazov.collectivepurchases.server.controllers.parser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kazov.collectivepurchases.server.common.ObjectMapper;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserCategory;
import ru.kazov.collectivepurchases.server.models.dto.parser.ParserCategoryDTO;
import ru.kazov.collectivepurchases.server.services.parser.ParserCategoryService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RequiredArgsConstructor
@Tag(name = "ParserCategory", description = "Parser category management API")
@RestController
@RequestMapping("/parser/catalog")
public class ParserCategoryController {

    private final ParserCategoryService parserCategoryService;
    private final ObjectMapper objectMapper;

    @Operation(summary = "Get all parser categories")
    @ApiResponse(responseCode = "200")
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<List<ParserCategoryDTO>> getAllParserCategories() {
        List<ParserCategory> parserCategoryDAOList = parserCategoryService.getParserCategories();
        List<ParserCategoryDTO> parserCategoryDTOList = objectMapper.convertParserCategories(parserCategoryDAOList);
        return ResponseEntity.ok(parserCategoryDTOList);
    }

    @Operation(summary = "Create new parser category")
    @ApiResponse(responseCode = "201")
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ParserCategoryDTO> createParserCategory(@RequestBody ParserCategoryDTO parserCategoryDTO) throws URISyntaxException {
        ParserCategory parserCategoryDAO = objectMapper.convertParserCategory(parserCategoryDTO);
        Long id = parserCategoryService.createParserCategory(parserCategoryDAO);
        ParserCategoryDTO persistParserCategoryDTO = objectMapper.convertParserCategory(parserCategoryDAO);
        return ResponseEntity.created(new URI("/parser/catalog/" + id)).body(persistParserCategoryDTO);
    }

    @Operation(summary = "Get parser category")
    @ApiResponse(responseCode = "200")
    @GetMapping(value = "/{parserCategoryId}", produces = "application/json")
    public ResponseEntity<ParserCategoryDTO> getParserCategory(@PathVariable Long parserCategoryId) {
        ParserCategory parserCategoryDAO = parserCategoryService.getParserCategory(parserCategoryId);
        ParserCategoryDTO parserCategoryDTO = objectMapper.convertParserCategory(parserCategoryDAO);
        return ResponseEntity.ok(parserCategoryDTO);
    }

    @Operation(summary = "Update parser category")
    @ApiResponse(responseCode = "200")
    @PutMapping(value = "/{parserCategoryId}", consumes = "application/json")
    public ResponseEntity<ParserCategoryDTO> updateParserCategory(@PathVariable Long parserCategoryId, @RequestBody ParserCategoryDTO parserCategoryDTO) {
        ParserCategory parserCategoryDAO = objectMapper.convertParserCategory(parserCategoryDTO);
        parserCategoryService.updateParserCategory(parserCategoryDAO, parserCategoryId);
        ParserCategoryDTO persistParserCategoryDTO = objectMapper.convertParserCategory(parserCategoryDAO);
        return ResponseEntity.ok(persistParserCategoryDTO);
    }

    @Operation(summary = "Delete parser category")
    @ApiResponse(responseCode = "204")
    @DeleteMapping(value = "/{parserCategoryId}")
    public ResponseEntity<Void> deleteParserCategory(@PathVariable Long parserCategoryId) {
        parserCategoryService.removeParserCategory(parserCategoryId);
        return ResponseEntity.noContent().build();
    }
}
