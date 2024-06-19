package com.angelfg.best_travel.infraestructure.services;

import com.angelfg.best_travel.api.dtos.request.ReservationRequest;
import com.angelfg.best_travel.api.dtos.response.HotelResponse;
import com.angelfg.best_travel.api.dtos.response.ReservationResponse;
import com.angelfg.best_travel.domain.entities.CustomerEntity;
import com.angelfg.best_travel.domain.entities.HotelEntity;
import com.angelfg.best_travel.domain.entities.ReservationEntity;
import com.angelfg.best_travel.domain.repositories.jpa.CustomerRepository;
import com.angelfg.best_travel.domain.repositories.jpa.HotelRepository;
import com.angelfg.best_travel.domain.repositories.jpa.ReservationRepository;
import com.angelfg.best_travel.infraestructure.abstract_services.ReservationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    public static final BigDecimal charges_price_percentage = BigDecimal.valueOf(0.20);

    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public ReservationResponse create(ReservationRequest request) {
        HotelEntity hotel = this.hotelRepository.findById(request.getIdHotel()).orElseThrow();
        CustomerEntity customer = this.customerRepository.findById(request.getIdClient()).orElseThrow();

        ReservationEntity reservationToPersist = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer)
                .totalDays(request.getTotalDays())
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(request.getTotalDays()))
                .price(hotel.getPrice().add(hotel.getPrice().multiply(charges_price_percentage)))
                .build();

        ReservationEntity reservationPersistend = this.reservationRepository.save(reservationToPersist);

        return this.entityToResponse(reservationPersistend);
    }

    @Override
    public ReservationResponse read(UUID uuid) {
        ReservationEntity reservationFromDB = this.reservationRepository
                .findById(uuid)
                .orElseThrow();

        return this.entityToResponse(reservationFromDB);
    }

    @Override
    public ReservationResponse update(ReservationRequest request, UUID id) {
        HotelEntity hotel = this.hotelRepository.findById(request.getIdHotel()).orElseThrow();
        ReservationEntity reservationToUpdate = this.reservationRepository.findById(id).orElseThrow();

        reservationToUpdate.setHotel(hotel);
        reservationToUpdate.setTotalDays(request.getTotalDays());
        reservationToUpdate.setDateTimeReservation(LocalDateTime.now());
        reservationToUpdate.setDateStart(LocalDate.now());
        reservationToUpdate.setDateEnd(LocalDate.now().plusDays(request.getTotalDays()));
        reservationToUpdate.setPrice(hotel.getPrice().add(hotel.getPrice().multiply(charges_price_percentage)));

        ReservationEntity reservationUpdated = this.reservationRepository.save(reservationToUpdate);;

        return this.entityToResponse(reservationUpdated);
    }

    @Override
    public void delete(UUID id) {
        ReservationEntity reservationToDelete = this.reservationRepository.findById(id).orElseThrow();
        this.reservationRepository.delete(reservationToDelete);
    }

    @Override
    public BigDecimal findPrice(Long hotelId) {
        HotelEntity hotelEntity = this.hotelRepository.findById(hotelId).orElseThrow();
        return hotelEntity.getPrice().add(hotelEntity.getPrice().multiply(charges_price_percentage));
    }

    // Realizar el mapeo entre entities a dtos
    private ReservationResponse entityToResponse(ReservationEntity entity) {
        ReservationResponse reservationResponse = new ReservationResponse();
        BeanUtils.copyProperties(entity, reservationResponse); // Copia las propiedades semejantes a la response

        HotelResponse hotelResponse = new HotelResponse();
        BeanUtils.copyProperties(entity.getHotel(), hotelResponse);

        reservationResponse.setHotel(hotelResponse);

        return reservationResponse;
    }

}
