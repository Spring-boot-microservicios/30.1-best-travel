package com.angelfg.best_travel.api.controllers;

import com.angelfg.best_travel.api.dtos.request.ReservationRequest;
import com.angelfg.best_travel.api.dtos.response.ErrorsResponse;
import com.angelfg.best_travel.api.dtos.response.ReservationResponse;
import com.angelfg.best_travel.infraestructure.abstract_services.ReservationService;
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

@RestController
@RequestMapping(path = "reservation")
@AllArgsConstructor
@Tag(name = "Reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @ApiResponse(
        responseCode = "400",
        description = "When the request have a field invalid we response this",
        content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResponse.class))
        }
    )
    @Operation(summary = "Save in system un reservation with the fly passed in parameter")
    @PostMapping
    public ResponseEntity<ReservationResponse> post(@Valid @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(this.reservationService.create(request));
    }

    @Operation(summary = "Return a reservation with of passed")
    @GetMapping(path = "/{id}")
    public ResponseEntity<ReservationResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(this.reservationService.read(id));
    }

    @Operation(summary = "Update reservation")
    @PutMapping(path = "{id}")
    public ResponseEntity<ReservationResponse> put(@Valid @PathVariable UUID id, @RequestBody ReservationRequest request) {
        return ResponseEntity.ok(this.reservationService.update(request, id));
    }

    @Operation(summary = "Delete a reservation with of passed")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        this.reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "return a reservation price given a hotel id")
    @GetMapping
    public ResponseEntity<Map<String, BigDecimal>> getHotelPrice(@RequestParam Long hotelId) {
        return ResponseEntity.ok(Collections.singletonMap("hotelPrice", this.reservationService.findPrice(hotelId)));
    }

}
