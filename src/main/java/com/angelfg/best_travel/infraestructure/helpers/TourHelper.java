package com.angelfg.best_travel.infraestructure.helpers;

import com.angelfg.best_travel.domain.entities.jpa.*;
import com.angelfg.best_travel.domain.repositories.jpa.ReservationRepository;
import com.angelfg.best_travel.domain.repositories.jpa.TicketRepository;
import com.angelfg.best_travel.util.BestTravelUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static com.angelfg.best_travel.infraestructure.services.ReservationServiceImpl.charges_price_percentage;
import static com.angelfg.best_travel.infraestructure.services.TicketServiceImpl.charger_price_percentage;

@Component
@Transactional
@AllArgsConstructor
@Slf4j
public class TourHelper {

    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;

    public Set<TicketEntity> createTickets(Set<FlyEntity> flights, CustomerEntity customer) {
        HashSet<TicketEntity> response = new HashSet<>(flights.size());

        flights.forEach(fly -> {
            TicketEntity ticketToPersist = TicketEntity.builder()
                    .id(UUID.randomUUID())
                    .fly(fly)
                    .customer(customer)
                    .price(fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage)))
                    .purchaseDate(LocalDate.now())
                    .departureDate(BestTravelUtil.getRandomSoon())
                    .arrivalDate(BestTravelUtil.getRandomLater())
                    .build();

            response.add(this.ticketRepository.save(ticketToPersist));
        });

        return response;
    }

    public Set<ReservationEntity> createReservations(HashMap<HotelEntity, Integer> hotels, CustomerEntity customer) {
        HashSet<ReservationEntity> response = new HashSet<>(hotels.size());

        hotels.forEach((hotel, totalDays) -> {
            ReservationEntity reservationToPersist = ReservationEntity.builder()
                    .id(UUID.randomUUID())
                    .hotel(hotel)
                    .customer(customer)
                    .totalDays(totalDays)
                    .dateTimeReservation(LocalDateTime.now())
                    .dateStart(LocalDate.now())
                    .dateEnd(LocalDate.now().plusDays(totalDays))
                    .price(hotel.getPrice().add(hotel.getPrice().multiply(charges_price_percentage)))
                    .build();

            response.add(this.reservationRepository.save(reservationToPersist));
        });

        return response;
    }

    public TicketEntity createTicket(FlyEntity fly, CustomerEntity customer) {

        TicketEntity ticketToPersist = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage)))
                .purchaseDate(LocalDate.now())
                .departureDate(BestTravelUtil.getRandomSoon())
                .arrivalDate(BestTravelUtil.getRandomLater())
                .build();

        return this.ticketRepository.save(ticketToPersist);
    }

    public ReservationEntity createReservation(HotelEntity hotel ,CustomerEntity customer, Integer totalDays) {
        ReservationEntity reservationToPersist = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .totalDays(totalDays)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(totalDays))
                .price(hotel.getPrice().add(hotel.getPrice().multiply(charges_price_percentage)))
                .build();

        return this.reservationRepository.save(reservationToPersist);
    }

}
