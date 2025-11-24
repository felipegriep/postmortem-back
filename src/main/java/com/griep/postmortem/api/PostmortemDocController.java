package com.griep.postmortem.api;

import com.griep.postmortem.domain.dto.response.PostmortemDocResponseDTO;
import com.griep.postmortem.domain.enums.DocDispositionEnum;
import com.griep.postmortem.domain.enums.DocFormatEnum;
import com.griep.postmortem.service.DocumentService;
import com.griep.postmortem.service.IPostmortemDocService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.griep.postmortem.domain.enums.DocDispositionEnum.valueOf;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequestUri;

@RestController
@RequestMapping("/api/incidents/{incidentId}/documents")
@RequiredArgsConstructor
@Validated
public class PostmortemDocController {

    private final IPostmortemDocService service;
    private final DocumentService documentService;

    @Operation(summary = "List Postmortem Docs of Incident", description = "List versions of postmortem docs of incident")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @GetMapping
    public ResponseEntity<List<PostmortemDocResponseDTO>> list(
            @PathVariable @Valid @NotNull @Positive final Long incidentId
    ) {
        return ok(service.list(incidentId));
    }

    @Operation(summary = "Get Postmortem Doc of Incident", description = "Get a specific version of postmortem doc of incident")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @GetMapping(
            value = "/{version}",
            produces = {
                    "text/markdown; charset=UTF-8",
                    "text/html; charset=UTF-8",
                    "application/pdf"
            })
    public ResponseEntity<byte[]> get(
            @PathVariable @Valid @NotNull @Positive final Long incidentId,
            @PathVariable @Valid @NotNull @Positive final Integer version,
            @RequestParam(required = false, defaultValue = "MD") final DocFormatEnum format,
            @RequestParam(required = false, defaultValue = "INLINE") final DocDispositionEnum disposition
    ) {
        var content = service.get(incidentId, version);

        content = documentService.convert(content, format);

        return fileResponse(
                content,
                format,
                documentService.filename(incidentId, version, format),
                disposition);
    }

    private ResponseEntity<byte[]> fileResponse(final byte[] content,
                                                final DocFormatEnum format,
                                                final String name,
                                                final DocDispositionEnum disposition) {
        return ResponseEntity.ok()
                .header(CONTENT_TYPE, format.getMimeType())
                .header(CONTENT_DISPOSITION, (valueOf(disposition)) + "; filename=\"" + name + "\"")
                .body(content);
    }

    @Operation(summary = "Postmortem Doc of Incident", description = "Generate a new version of postmortem doc of incident")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    @PostMapping
    public ResponseEntity<Void> create(@PathVariable @Valid @NotNull @Positive final Long incidentId) {
        var version = service.create(incidentId);
        var location = fromCurrentRequestUri()
                .path("/{version}")
                .buildAndExpand(version)
                .toUri();

        return created(location).build();
    }
}