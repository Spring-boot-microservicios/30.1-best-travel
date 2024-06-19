package com.angelfg.best_travel.infraestructure.abstract_services;

import com.angelfg.best_travel.api.dtos.request.ReservationRequest;
import com.angelfg.best_travel.api.dtos.response.ReservationResponse;

import java.util.UUID;

public interface ReservationService extends CrudService<ReservationRequest, ReservationResponse, UUID> {

}
