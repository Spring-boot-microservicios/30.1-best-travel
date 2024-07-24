package com.angelfg.best_travel.infraestructure.services;

import com.angelfg.best_travel.api.models.request.TourRequest;
import com.angelfg.best_travel.api.models.response.TourResponse;
import com.angelfg.best_travel.domain.entities.jpa.*;
import com.angelfg.best_travel.domain.repositories.jpa.CustomerRepository;
import com.angelfg.best_travel.domain.repositories.jpa.FlyRepository;
import com.angelfg.best_travel.domain.repositories.jpa.HotelRepository;
import com.angelfg.best_travel.domain.repositories.jpa.TourRepository;
import com.angelfg.best_travel.infraestructure.abstract_services.TourService;
import com.angelfg.best_travel.infraestructure.helpers.BlackListHelper;
import com.angelfg.best_travel.infraestructure.helpers.CustomerHelper;
import com.angelfg.best_travel.infraestructure.helpers.EmailHelper;
import com.angelfg.best_travel.infraestructure.helpers.TourHelper;
import com.angelfg.best_travel.util.enums.Tables;
import com.angelfg.best_travel.util.exceptions.IdNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final FlyRepository flyRepository;
    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final TourHelper tourHelper;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;
    private final EmailHelper emailHelper;

    @Override
    public void removeTicket(Long tourId, UUID ticketId) {
        TourEntity tourUpdate = this.tourRepository.findById(tourId).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        tourUpdate.removeTicket(ticketId);
        this.tourRepository.save(tourUpdate);
    }

    @Override
    public UUID addTicket(Long tourId, Long flyId) {
        TourEntity tourEntity = this.tourRepository.findById(tourId).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        FlyEntity fly = this.flyRepository.findById(flyId).orElseThrow(() -> new IdNotFoundException(Tables.fly.name()));
        TicketEntity ticket = this.tourHelper.createTicket(fly, tourEntity.getCustomer());
        tourEntity.addTicket(ticket);
        this.tourRepository.save(tourEntity);

        return ticket.getId();
    }

    @Override
    public void removeReservation(Long tourId, UUID reservationId) {
        TourEntity tourUpdate = this.tourRepository.findById(tourId).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        tourUpdate.removeReservation(reservationId);
        this.tourRepository.save(tourUpdate);
    }

    @Override
    public UUID addReservation(Long hotelId, Long tourId, Integer totalDays) {
        TourEntity tourUpdate = this.tourRepository.findById(tourId).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        HotelEntity hotel = this.hotelRepository.findById(hotelId).orElseThrow(() -> new IdNotFoundException(Tables.hotel.name()));
        ReservationEntity reservation = this.tourHelper.createReservation(hotel, tourUpdate.getCustomer(), totalDays);
        tourUpdate.addReservation(reservation);
        this.tourRepository.save(tourUpdate);

        return reservation.getId();
    }

    @Override
    public TourResponse create(TourRequest request) {
        this.blackListHelper.isInBlackListCustomer(request.getCustomerId());

        CustomerEntity customer = this.customerRepository.findById(request.getCustomerId()).orElseThrow(() -> new IdNotFoundException(Tables.customer.name()));

        HashSet<FlyEntity> flights = new HashSet<>();
        request.getFlights().forEach(fly -> flights.add(this.flyRepository.findById(fly.getId()).orElseThrow(() -> new IdNotFoundException(Tables.fly.name()))));

        HashMap<HotelEntity, Integer> hotels = new HashMap<>();
        request.getHotels().forEach(hotel -> hotels.put(this.hotelRepository.findById(hotel.getId()).orElseThrow(() -> new IdNotFoundException(Tables.hotel.name())), hotel.getTotalDays()));

        TourEntity tourToSave = TourEntity.builder()
                .tickets(this.tourHelper.createTickets(flights, customer))
                .reservations(this.tourHelper.createReservations(hotels, customer))
                .customer(customer)
                .build();

        TourEntity tourSaved = this.tourRepository.save(tourToSave);

        this.customerHelper.increase(customer.getDni(), TourServiceImpl.class);

        if (Objects.nonNull(request.getEmail())) this.emailHelper.sendMail(request.getEmail(), customer.getFullName(), Tables.tour.name());

        return TourResponse.builder()
                .id(tourSaved.getId())
                .reservationIds(tourSaved.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
                .ticketIds(tourSaved.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public TourResponse read(Long id) {
        TourEntity tourFromDB = this.tourRepository.findById(id).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));

        return TourResponse.builder()
                .id(tourFromDB.getId())
                .reservationIds(tourFromDB.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
                .ticketIds(tourFromDB.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public TourResponse update(TourRequest request, Long aLong) {
        return null;
    }

    @Override
    public void delete(Long id) {
        TourEntity tourToDelete = this.tourRepository.findById(id).orElseThrow(() -> new IdNotFoundException(Tables.tour.name()));
        this.tourRepository.delete(tourToDelete);
    }

}
