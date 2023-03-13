package ru.kazov.collectivepurchases.server.controllers.sale;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kazov.collectivepurchases.server.common.ObjectMapper;
import ru.kazov.collectivepurchases.server.models.dao.sale.SaleItem;
import ru.kazov.collectivepurchases.server.models.dto.sale.SaleItemDTO;
import ru.kazov.collectivepurchases.server.services.sale.SaleItemService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RequiredArgsConstructor
@Tag(name = "SaleItem", description = "Sale item management API")
@RestController
@RequestMapping("/catalog/{saleId}/categories/{saleCategoryId}/items")
public class SaleItemController {
    private final ObjectMapper objectMapper;
    private final SaleItemService saleItemService;

    @Operation(summary = "Get all sale items")
    @ApiResponse(responseCode = "200")
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<List<SaleItemDTO>> getAllSaleItems(@PathVariable Long saleId, @PathVariable Long saleCategoryId) {
        List<SaleItem> saleItemDAOList = saleItemService.getSaleItems(saleCategoryId);
        List<SaleItemDTO> saleItemDTOList = objectMapper.convertSaleItems(saleItemDAOList);
        return ResponseEntity.ok(saleItemDTOList);
    }

    @Operation(summary = "Create new sale item")
    @ApiResponse(responseCode = "201")
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<SaleItemDTO> createSaleItem(@PathVariable Long saleId, @PathVariable Long saleCategoryId, @RequestBody SaleItemDTO saleItemDTO) throws URISyntaxException {
        SaleItem saleItemDAO = objectMapper.convertSaleItem(saleItemDTO);
        Long id = saleItemService.createSaleItem(saleItemDAO, saleCategoryId);
        SaleItemDTO persistSaleItemDTO = objectMapper.convertSaleItem(saleItemDAO);
        return ResponseEntity.created(new URI("/catalog/" + saleId + "/category/" + saleCategoryId + "/items/" + id)).body(persistSaleItemDTO);
    }

    @Operation(summary = "Get sale item")
    @ApiResponse(responseCode = "200")
    @GetMapping(value = "/{saleItemId}", produces = "application/json")
    public ResponseEntity<SaleItemDTO> getSaleItem(@PathVariable Long saleId, @PathVariable Long saleCategoryId, @PathVariable Long saleItemId) {
        SaleItem saleItemDAO = saleItemService.getSaleItem(saleItemId);
        SaleItemDTO saleItemDTO = objectMapper.convertSaleItem(saleItemDAO);
        return ResponseEntity.ok(saleItemDTO);
    }

    @Operation(summary = "Update sale item")
    @ApiResponse(responseCode = "200")
    @PutMapping(value = "/{saleItemId}", consumes = "application/json")
    public ResponseEntity<SaleItemDTO> updateSaleItem(@PathVariable Long saleId, @PathVariable Long saleCategoryId, @PathVariable Long saleItemId, @RequestBody SaleItemDTO saleItemDTO) {
        SaleItem saleItemDAO = objectMapper.convertSaleItem(saleItemDTO);
        saleItemService.updateSaleItem(saleItemDAO, saleItemId);
        SaleItemDTO persistSaleItemDTO = objectMapper.convertSaleItem(saleItemDAO);
        return ResponseEntity.ok(persistSaleItemDTO);
    }

    @Operation(summary = "Delete sale item")
    @ApiResponse(responseCode = "204")
    @DeleteMapping(value = "/{saleItemId}")
    public ResponseEntity<Void> deleteSaleCategory(@PathVariable Long saleId, @PathVariable Long saleCategoryId, @PathVariable Long saleItemId) {
        saleItemService.removeSaleItem(saleItemId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Batch delete parser item")
    @ApiResponse(responseCode = "204")
    @DeleteMapping(value = "/batch", consumes = "application/json")
    public ResponseEntity<Void> batchDeleteSaleItem(@PathVariable Long saleId, @PathVariable Long saleCategoryId, @RequestBody List<Long> ids) {
        saleItemService.batchRemoveSaleItems(ids);
        return ResponseEntity.noContent().build();
    }
}
