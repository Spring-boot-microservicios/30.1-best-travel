package com.angelfg.best_travel.api.controllers;

import com.angelfg.best_travel.api.dtos.request.TourRequest;
import com.angelfg.best_travel.api.dtos.response.TourResponse;
import com.angelfg.best_travel.infraestructure.abstract_services.TourService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<TourResponse> delete(@PathVariable Long id) {
        this.tourService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
