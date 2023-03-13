package ru.kazov.collectivepurchases.server.controllers.sale;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kazov.collectivepurchases.server.common.ObjectMapper;
import ru.kazov.collectivepurchases.server.models.dao.sale.SaleCategory;
import ru.kazov.collectivepurchases.server.models.dto.sale.SaleCategoryDTO;
import ru.kazov.collectivepurchases.server.services.sale.SaleCategoryService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RequiredArgsConstructor
@Tag(name = "SaleCategory", description = "Sale category management API")
@RestController
@RequestMapping("/catalog/{saleId}/categories/")
public class SaleCategoryController {
    private final SaleCategoryService saleCategoryService;
    private final ObjectMapper objectMapper;

    @Operation(summary = "Get all sale categories")
    @ApiResponse(responseCode = "200")
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<List<SaleCategoryDTO>> getAllSaleCategories(@PathVariable Long saleId) {
        List<SaleCategory> saleCategoryDAOList = saleCategoryService.getSaleCategories(saleId);
        List<SaleCategoryDTO> saleCategoryDTOList = objectMapper.convertSaleCategories(saleCategoryDAOList);
        return ResponseEntity.ok(saleCategoryDTOList);
    }

    @Operation(summary = "Create new sale category")
    @ApiResponse(responseCode = "201")
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<SaleCategoryDTO> createSaleCategory(@PathVariable Long saleId, @RequestBody SaleCategoryDTO saleCategoryDTO) throws URISyntaxException {
        SaleCategory saleCategoryDAO = objectMapper.convertSaleCategory(saleCategoryDTO);
        Long id = saleCategoryService.createSaleCategory(saleCategoryDAO, saleId);
        SaleCategoryDTO persistSaleCategoryDTO = objectMapper.convertSaleCategory(saleCategoryDAO);
        return ResponseEntity.created(new URI("/catalog/" + saleId + "/category/" + id)).body(persistSaleCategoryDTO);
    }

    @Operation(summary = "Get sale category")
    @ApiResponse(responseCode = "200")
    @GetMapping(value = "/{saleCategoryId}", produces = "application/json")
    public ResponseEntity<SaleCategoryDTO> getSaleCategory(@PathVariable Long saleId, @PathVariable Long saleCategoryId) {
        SaleCategory saleCategoryDAO = saleCategoryService.getSaleCategory(saleCategoryId);
        SaleCategoryDTO saleCategoryDTO = objectMapper.convertSaleCategory(saleCategoryDAO);
        return ResponseEntity.ok(saleCategoryDTO);
    }

    @Operation(summary = "Update sale category")
    @ApiResponse(responseCode = "200")
    @PutMapping(value = "/{saleCategoryId}", consumes = "application/json")
    public ResponseEntity<SaleCategoryDTO> updateSaleCategory(@RequestBody SaleCategoryDTO saleCategoryDTO, @PathVariable Long saleCategoryId, @PathVariable Long saleId) {
        SaleCategory saleCategoryDAO = objectMapper.convertSaleCategory(saleCategoryDTO);
        saleCategoryService.updateSaleCategory(saleCategoryDAO, saleCategoryId);
        SaleCategoryDTO persistSaleCategoryDTO = objectMapper.convertSaleCategory(saleCategoryDAO);
        return ResponseEntity.ok(persistSaleCategoryDTO);
    }

    @Operation(summary = "Delete sale category")
    @ApiResponse(responseCode = "204")
    @DeleteMapping(value = "/{saleCategoryId}")
    public ResponseEntity<Void> deleteSaleCategory(@PathVariable Long saleId, @PathVariable Long saleCategoryId) {
        saleCategoryService.removeSaleCategory(saleCategoryId);
        return ResponseEntity.noContent().build();
    }

}
