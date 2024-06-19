package com.angelfg.best_travel.api.controllers;

import com.angelfg.best_travel.api.dtos.request.ReservationRequest;
import com.angelfg.best_travel.api.dtos.response.ReservationResponse;
import com.angelfg.best_travel.infraestructure.abstract_services.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "reservation")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponse> post(@RequestBody ReservationRequest request) {
        return ResponseEntity.ok(this.reservationService.create(request));
    }

}
