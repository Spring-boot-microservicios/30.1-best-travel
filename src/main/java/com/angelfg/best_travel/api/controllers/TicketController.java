package com.angelfg.best_travel.api.controllers;

import com.angelfg.best_travel.api.dtos.request.TicketRequest;
import com.angelfg.best_travel.api.dtos.response.ErrorsResponse;
import com.angelfg.best_travel.api.dtos.response.TicketResponse;
import com.angelfg.best_travel.infraestructure.abstract_services.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

// @Controller => para cuando usamos el MVC para vistas
@RestController
@RequestMapping(path = "ticket")
@AllArgsConstructor
@Tag(name = "Ticket")
public class TicketController {

    private final TicketService ticketService;

    @ApiResponse(
        responseCode = "400",
        description = "When the request have a field invalid we response this",
        content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResponse.class))
        }
    )
    @Operation(summary = "Save in system un ticket with the fly passed in parameter")
    @PostMapping
    public ResponseEntity<TicketResponse> create(@Valid @RequestBody TicketRequest ticketRequest) {
        return ResponseEntity.ok(this.ticketService.create(ticketRequest));
    }

    @Operation(summary = "Return a ticket with of passed")
    @GetMapping(path = "/{id}")
    public ResponseEntity<TicketResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(this.ticketService.read(id));
    }

    @Operation(summary = "Update ticket")
    @PutMapping(path = "/{id}")
    public ResponseEntity<TicketResponse> put(@Valid @PathVariable UUID id, @RequestBody TicketRequest ticketRequest) {
        return ResponseEntity.ok(this.ticketService.update(ticketRequest, id));
    }

    @Operation(summary = "Delete a ticket with of passed")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Map<String, BigDecimal>> getFlyPrice(@RequestParam Long flyId) {
        return ResponseEntity.ok(Collections.singletonMap("flyPrice", this.ticketService.findPrice(flyId)));
    }

}
