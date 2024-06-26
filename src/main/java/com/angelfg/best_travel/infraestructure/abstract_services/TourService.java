package com.angelfg.best_travel.infraestructure.abstract_services;

import com.angelfg.best_travel.api.dtos.request.TourRequest;
import com.angelfg.best_travel.api.dtos.response.TourResponse;

import java.util.UUID;

public interface TourService extends CrudService<TourRequest, TourResponse, Long> {
    void removeTicket(UUID ticketId, Long tourId);
    UUID addTicket(Long flyId, Long tourId);
    void removeReservation(UUID reservationId, Long tourId);
    UUID addReservation(Long reservationId, Long tourId);
}
