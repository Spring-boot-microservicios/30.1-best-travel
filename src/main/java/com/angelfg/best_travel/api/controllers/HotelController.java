package com.angelfg.best_travel.api.controllers;

import com.angelfg.best_travel.api.dtos.response.HotelResponse;
import com.angelfg.best_travel.infraestructure.abstract_services.HotelService;
import com.angelfg.best_travel.util.enums.SortType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping(path = "hotel")
@AllArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<Page<HotelResponse>> getAll(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestHeader(required = false) SortType sortType
    ) {
        if (Objects.isNull(sortType)) sortType = SortType.NONE;
        Page<HotelResponse> response = this.hotelService.readAll(page, size, sortType);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping(path = "less_price")
    public ResponseEntity<Set<HotelResponse>> getLessPrice(
        @RequestParam BigDecimal price
    ) {
        Set <HotelResponse> response = this.hotelService.readLessPrice(price);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping(path = "between_price")
    public ResponseEntity<Set<HotelResponse>> getBetweenPrice(
        @RequestParam BigDecimal min,
        @RequestParam BigDecimal max
    ) {
        Set <HotelResponse> response = this.hotelService.readBetweenPrice(min, max);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @GetMapping(path = "rating")
    public ResponseEntity<Set<HotelResponse>> getByRaiting(
        @RequestParam Integer rating
    ) {
        if (rating > 4) rating = 4;
        if (rating < 1) rating = 1;

        Set <HotelResponse> response = this.hotelService.readByRaiting(rating);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

}
