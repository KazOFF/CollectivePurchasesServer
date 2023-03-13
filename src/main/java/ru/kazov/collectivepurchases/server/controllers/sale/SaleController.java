package ru.kazov.collectivepurchases.server.controllers.sale;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kazov.collectivepurchases.server.common.ObjectMapper;
import ru.kazov.collectivepurchases.server.models.dao.sale.Sale;
import ru.kazov.collectivepurchases.server.models.dto.ErrorResponse;
import ru.kazov.collectivepurchases.server.models.dto.sale.PictureResponse;
import ru.kazov.collectivepurchases.server.models.dto.sale.SaleDTO;
import ru.kazov.collectivepurchases.server.services.PictureService;
import ru.kazov.collectivepurchases.server.services.sale.SaleService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RequiredArgsConstructor
@Tag(name = "Sale", description = "Sale management API")
@RestController
@RequestMapping("/catalog")
public class SaleController {
    private final SaleService saleService;
    private final ObjectMapper objectMapper;
    private final PictureService pictureService;

    @Operation(summary = "Gets all sales")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<List<SaleDTO>> getAllSales() {
        List<Sale> saleDAOList = saleService.getSales();
        List<SaleDTO> saleDTOList = objectMapper.convertSales(saleDAOList);
        return ResponseEntity.ok(saleDTOList);
    }

    @Operation(summary = "Create new sale")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
    })
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<SaleDTO> createSale(@RequestBody SaleDTO saleDTO) throws URISyntaxException {
        Sale saleDAO = objectMapper.convertSale(saleDTO);
        Long id = saleService.createSale(saleDAO);
        SaleDTO persistSaleDTO = objectMapper.convertSale(saleDAO);
        return ResponseEntity.created(new URI("/catalog/" + id)).body(persistSaleDTO);
    }

    @PostMapping(value = "/picture")
    public ResponseEntity<PictureResponse> uploadPicture(@RequestParam("file") MultipartFile file){
        PictureResponse pictureResponse = new PictureResponse();
        pictureResponse.setPicture(pictureService.storePicture(file));
        return ResponseEntity.ok(pictureResponse);
    }

    @Operation(summary = "Get sale")
    @ApiResponse(responseCode = "200")
    @GetMapping(value = "/{saleId}", produces = "application/json")
    public ResponseEntity<SaleDTO> getSale(@PathVariable Long saleId) {
        Sale saleDAO = saleService.getSale(saleId);
        SaleDTO saleDTO = objectMapper.convertSale(saleDAO);
        return ResponseEntity.ok(saleDTO);
    }

    @Operation(summary = "Update sale")
    @ApiResponse(responseCode = "200")
    @PutMapping(value = "/{saleId}", consumes = "application/json")
    public ResponseEntity<SaleDTO> updateSale(@RequestBody SaleDTO saleDTO, @PathVariable Long saleId) {
        Sale saleDAO = objectMapper.convertSale(saleDTO);
        saleService.updateSale(saleId, saleDAO);
        SaleDTO persistSaleDTO = objectMapper.convertSale(saleDAO);
        return ResponseEntity.ok(persistSaleDTO);
    }

    @Operation(summary = "Delete sale")
    @ApiResponse(responseCode = "204")
    @DeleteMapping(value = "/{saleId}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long saleId) {
        saleService.removeSale(saleId);
        return ResponseEntity.noContent().build();
    }
}
