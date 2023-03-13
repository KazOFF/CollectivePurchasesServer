package ru.kazov.collectivepurchases.server.controllers.parser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kazov.collectivepurchases.server.common.ObjectMapper;
import ru.kazov.collectivepurchases.server.models.dao.parser.ParserJob;
import ru.kazov.collectivepurchases.server.models.dto.parser.ParserJobDTO;
import ru.kazov.collectivepurchases.server.services.parser.ParserJobService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RequiredArgsConstructor
@Tag(name = "ParserJob", description = "Parser job management API")
@RestController
@RequestMapping("parser/job")
public class ParserJobController {
    private final ObjectMapper objectMapper;
    private final ParserJobService parserJobService;

    @Operation(summary = "Get all parser jobs")
    @ApiResponse(responseCode = "200")
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<List<ParserJobDTO>> getAllParserJobs() {
        List<ParserJob> parserJobDAOList = parserJobService.getAllParserJobs();
        List<ParserJobDTO> parserJobDTOList = objectMapper.convertParserJobs(parserJobDAOList);
        return ResponseEntity.ok(parserJobDTOList);
    }

    @Operation(summary = "Create new parser job")
    @ApiResponse(responseCode = "201")
    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ParserJobDTO> createParserJob(@RequestBody ParserJobDTO parserJobDTO) throws URISyntaxException {
        ParserJob parserJobDAO = objectMapper.convertParserJob(parserJobDTO);
        Long id = parserJobService.createParserJob(parserJobDAO);
        ParserJobDTO persistParserJob = objectMapper.convertParserJob(parserJobDAO);
        return ResponseEntity.created(new URI("/parser/job/" + id)).body(persistParserJob);
    }

    @Operation(summary = "Get parser job")
    @ApiResponse(responseCode = "200")
    @GetMapping(value = "/{parserJobId}", produces = "application/json")
    public ResponseEntity<ParserJobDTO> getParserJob(@PathVariable Long parserJobId) {
        ParserJob parserJobDAO = parserJobService.getParserJob(parserJobId);
        ParserJobDTO parserJobDTO = objectMapper.convertParserJob(parserJobDAO);
        return ResponseEntity.ok(parserJobDTO);
    }

    @Operation(summary = "Set job's status PENDING")
    @ApiResponse(responseCode = "204")
    @PatchMapping("/{parserJobId}/refresh")
    public ResponseEntity<Void> refreshParserJob(@PathVariable Long parserJobId) {
        parserJobService.setPending(parserJobId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete parser job")
    @ApiResponse(responseCode = "204")
    @DeleteMapping(value = "/{parserJobId}")
    public ResponseEntity<Void> deleteParserJob(@PathVariable Long parserJobId) {
        parserJobService.removeParserJob(parserJobId);
        return ResponseEntity.noContent().build();
    }
}
