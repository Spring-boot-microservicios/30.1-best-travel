package com.angelfg.best_travel.api.controllers;

import com.angelfg.best_travel.api.dtos.request.TourRequest;
import com.angelfg.best_travel.api.dtos.response.TourResponse;
import com.angelfg.best_travel.infraestructure.abstract_services.TourService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "tour")
@AllArgsConstructor
public class TourController {

    private final TourService tourService;

    @PostMapping
    public ResponseEntity<TourResponse> post(@RequestBody TourRequest request) {
        return ResponseEntity.ok(this.tourService.create(request));
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<TourResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(this.tourService.read(id));
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.tourService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "{tourId}/remove-ticket/{ticketId}")
    public ResponseEntity<TourResponse> deleteTicket(@PathVariable Long tourId, @PathVariable UUID ticketId) {
        this.tourService.removeTicket(tourId, ticketId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = "{tourId}/add-ticket/{flyId}")
    public ResponseEntity<Map<String, UUID>> postTicket(@PathVariable Long tourId, @PathVariable Long flyId) {
        Map<String, UUID> response = Collections.singletonMap("ticketId", this.tourService.addTicket(tourId, flyId));
        return ResponseEntity.ok(response);
    }

    @PatchMapping(path = "{tourId}/remove-reservation/{reservationId}")
    public ResponseEntity<TourResponse> deleteReservation(@PathVariable Long tourId, @PathVariable UUID reservationId) {
        this.tourService.removeReservation(tourId, reservationId);
        return ResponseEntity.noContent().build();
    }

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
