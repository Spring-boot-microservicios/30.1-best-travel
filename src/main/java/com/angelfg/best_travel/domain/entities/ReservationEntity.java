package com.angelfg.best_travel.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "reservation")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservationEntity implements Serializable {

    @Id
    private UUID id;

    @Column(name = "date_reservation")
    private LocalDateTime dateTimeReservation;

    private LocalDate dateStart;
    private LocalDate dateEnd;
    private Integer totalDays;
    private BigDecimal price;

    // Muchas reservaciones se encuentran en un hotel
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotel;

    // Muchas reservaciones pueden tener un tour y ser nullable
    @ManyToOne
    @JoinColumn(name = "tour_id", nullable = true)
    private TourEntity tour;

}
