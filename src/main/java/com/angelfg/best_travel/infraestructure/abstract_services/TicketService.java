package com.angelfg.best_travel.infraestructure.abstract_services;

import com.angelfg.best_travel.api.dtos.request.TicketRequest;
import com.angelfg.best_travel.api.dtos.response.TicketResponse;

import java.math.BigDecimal;
import java.util.UUID;

public interface TicketService extends CrudService<TicketRequest, TicketResponse, UUID> {
    BigDecimal findPrice(Long flyId);
}
