package com.angelfg.best_travel.api.controllers;

import com.angelfg.best_travel.api.dtos.request.TicketRequest;
import com.angelfg.best_travel.api.dtos.response.TicketResponse;
import com.angelfg.best_travel.infraestructure.abstract_services.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @Controller => para cuando usamos el MVC para vistas
@RestController
@RequestMapping(path = "ticket")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponse> create(@RequestBody TicketRequest ticketRequest) {
        return ResponseEntity.ok(this.ticketService.create(ticketRequest));
    }

}
