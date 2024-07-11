package com.angelfg.best_travel.infraestructure.abstract_services;

import com.angelfg.best_travel.api.models.request.ReservationRequest;
import com.angelfg.best_travel.api.models.response.ReservationResponse;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

public interface ReservationService extends CrudService<ReservationRequest, ReservationResponse, UUID> {
    BigDecimal findPrice(Long hotelId, Currency currency);
}
