package com.angelfg.best_travel.infraestructure.abstract_services;

import com.angelfg.best_travel.api.dtos.request.TourRequest;
import com.angelfg.best_travel.api.dtos.response.TourResponse;

import java.util.UUID;

public interface TourService extends CrudService<TourRequest, TourResponse, Long> {
    void removeTicket(Long tourId, UUID ticketId);
    UUID addTicket(Long tourId, Long flyId);
    void removeReservation(Long tourId, UUID reservationId);
    UUID addReservation(Long reservationId, Long tourId);
}
