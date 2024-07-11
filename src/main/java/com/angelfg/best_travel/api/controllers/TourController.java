package com.angelfg.best_travel.api.controllers;

import com.angelfg.best_travel.api.models.request.TourRequest;
import com.angelfg.best_travel.api.models.response.ErrorsResponse;
import com.angelfg.best_travel.api.models.response.TourResponse;
import com.angelfg.best_travel.infraestructure.abstract_services.TourService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "tour")
@AllArgsConstructor
@Tag(name = "Tour")
public class TourController {

    private final TourService tourService;

    @ApiResponse(
        responseCode = "400",
        description = "When the request have a field invalid we response this",
        content = {
                @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResponse.class))
        }
    )
    @Operation(summary = "Save in system one tour based in list of hotels and flights")
    @PostMapping
    public ResponseEntity<TourResponse> post(@Valid @RequestBody TourRequest request) {
        return ResponseEntity.ok(this.tourService.create(request));
    }

    @Operation(summary = "Return a Tour with id ad passed")
    @GetMapping(path = "{id}")
    public ResponseEntity<TourResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(this.tourService.read(id));
    }

    @Operation(summary = "Delete a Tour with id ad passed")
    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.tourService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove a ticket from tour")
    @PatchMapping(path = "{tourId}/remove-ticket/{ticketId}")
    public ResponseEntity<TourResponse> deleteTicket(@PathVariable Long tourId, @PathVariable UUID ticketId) {
        this.tourService.removeTicket(tourId, ticketId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add a ticket from tour")
    @PatchMapping(path = "{tourId}/add-ticket/{flyId}")
    public ResponseEntity<Map<String, UUID>> postTicket(@PathVariable Long tourId, @PathVariable Long flyId) {
        Map<String, UUID> response = Collections.singletonMap("ticketId", this.tourService.addTicket(tourId, flyId));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remove a reservation from tour")
    @PatchMapping(path = "{tourId}/remove-reservation/{reservationId}")
    public ResponseEntity<TourResponse> deleteReservation(@PathVariable Long tourId, @PathVariable UUID reservationId) {
        this.tourService.removeReservation(tourId, reservationId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add a reservation from tour")
    @PatchMapping(path = "{tourId}/add-reservation/{hotelId}")
    public ResponseEntity<Map<String, UUID>> postReservation(
        @PathVariable Long tourId,
        @PathVariable Long hotelId,
        @RequestParam Integer totalDays
    ) {
        Map<String, UUID> response = Collections.singletonMap("reservationId", this.tourService.addReservation(hotelId, tourId, totalDays));
        return ResponseEntity.ok(response);
    }

}
