package com.angelfg.best_travel.domain.entities.jpa;

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

@Entity(name = "ticket")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketEntity implements Serializable {

    @Id
    private UUID id;

    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private LocalDate purchaseDate;
    private BigDecimal price;

    // Muchos tickets pueden contener un vuelo
    @ManyToOne(fetch = FetchType.LAZY) // Sino es LAZY no borrara con el delete
    @JoinColumn(name = "fly_id")
    private FlyEntity fly;

    // Muchos tickets pueden tener un tour y ser nullable
    @ManyToOne(fetch = FetchType.LAZY) // Sino es LAZY no borrara con el delete
    @JoinColumn(name = "tour_id", nullable = true)
    private TourEntity tour;

    // Muchos tickets pueden contener un customer
    @ManyToOne(fetch = FetchType.LAZY) // Sino es LAZY no borrara con el delete
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

}
