package com.angelfg.best_travel.api.controllers;

import com.angelfg.best_travel.api.dtos.response.FlyResponse;
import com.angelfg.best_travel.infraestructure.abstract_services.FlyService;
import com.angelfg.best_travel.util.enums.SortType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping(path = "fly")
@AllArgsConstructor
public class FlyController {

    private final FlyService flyService;

    @GetMapping
    public ResponseEntity<Page<FlyResponse>> getAll(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestHeader(required = false) SortType sortType
    ) {
        if (Objects.isNull(sortType)) sortType = SortType.NONE;
        Page<FlyResponse> response = this.flyService.readAll(page, size, sortType);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping(path = "less_price")
    public ResponseEntity<Set<FlyResponse>> getLessPrice(
        @RequestParam BigDecimal price
    ) {
        Set <FlyResponse> response = this.flyService.readLessPrice(price);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping(path = "between_price")
    public ResponseEntity<Set<FlyResponse>> getBetweenPrice(
        @RequestParam BigDecimal min,
        @RequestParam BigDecimal max
    ) {
        Set <FlyResponse> response = this.flyService.readBetweenPrice(min, max);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping(path = "origin_destiny")
    public ResponseEntity<Set<FlyResponse>> getByOriginDestiny(
        @RequestParam String origin,
        @RequestParam String destiny
    ) {
        Set <FlyResponse> response = this.flyService.readByOriginDestiny(origin, destiny);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

}
