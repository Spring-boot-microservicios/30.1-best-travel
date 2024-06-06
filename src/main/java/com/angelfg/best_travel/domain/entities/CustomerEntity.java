package com.angelfg.best_travel.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Entity(name = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerEntity implements Serializable {

    @Id
    private String dni;

    @Column(length = 50)
    private String fullName;

    @Column(length = 20)
    private String creditCard;

    @Column(length = 12)
    private String phoneNumber;

    private Integer totalFlights;
    private Integer totalLodgings;
    private Integer totalTours;

    // Un customer puede contener muchos tickets
    @OneToMany(
        mappedBy = "customer",
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER, // Trae todo el join
        orphanRemoval = true // Eliminar los datos huerffanos
    )
    private Set<TicketEntity> tickets;

    // Un customer puede contener muchas reservations
    @OneToMany(
        mappedBy = "customer",
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER, // Trae todo el join
        orphanRemoval = true // Eliminar los datos huerffanos
    )
    private Set<ReservationEntity> reservations;

    // Un customer puede contener muchos tours
    @OneToMany(
        mappedBy = "customer",
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER, // Trae todo el join
        orphanRemoval = true // Eliminar los datos huerffanos
    )
    private Set<TourEntity> tours;

}
