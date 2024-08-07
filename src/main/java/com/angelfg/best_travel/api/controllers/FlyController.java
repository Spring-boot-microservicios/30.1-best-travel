package com.angelfg.best_travel.api.controllers;

import com.angelfg.best_travel.api.models.response.FlyResponse;
import com.angelfg.best_travel.infraestructure.abstract_services.FlyService;
import com.angelfg.best_travel.util.annotations.Notify;
import com.angelfg.best_travel.util.enums.SortType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping(path = "fly")
@AllArgsConstructor
@Tag(name = "Fly")
public class FlyController {

    private final FlyService flyService;

    @Notify(value = "GET fly")
    @Operation(summary = "Return a page with flights can be sorted or not")
    @GetMapping
    public ResponseEntity<Page<FlyResponse>> getAll(
        @RequestParam(defaultValue = "0") Integer page,
        @RequestParam(defaultValue = "10") Integer size,
        @RequestHeader(required = false) SortType sortType
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("authentication:: " + authentication.getAuthorities());

        if (Objects.isNull(sortType)) sortType = SortType.NONE;
        Page<FlyResponse> response = this.flyService.readAll(page, size, sortType);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Return a list with flights with price less to price in parameter")
    @GetMapping(path = "less_price")
    public ResponseEntity<Set<FlyResponse>> getLessPrice(
        @RequestParam BigDecimal price
    ) {
        Set <FlyResponse> response = this.flyService.readLessPrice(price);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Return a list with flights with between prices in parameters")
    @GetMapping(path = "between_price")
    public ResponseEntity<Set<FlyResponse>> getBetweenPrice(
        @RequestParam BigDecimal min,
        @RequestParam BigDecimal max
    ) {
        Set <FlyResponse> response = this.flyService.readBetweenPrice(min, max);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

    @Operation(summary = "Return a list with flights with between origin and destiny in parameters")
    @GetMapping(path = "origin_destiny")
    public ResponseEntity<Set<FlyResponse>> getByOriginDestiny(
        @RequestParam String origin,
        @RequestParam String destiny
    ) {
        Set <FlyResponse> response = this.flyService.readByOriginDestiny(origin, destiny);
        return response.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(response);
    }

}
