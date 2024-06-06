package com.angelfg.best_travel.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Entity(name = "tour")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Un tour puede contener muchas reservaciones
    @OneToMany(
        mappedBy = "tour",
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER, // Trae todo el join
        orphanRemoval = true // Eliminar los datos huerffanos
    )
    private Set<ReservationEntity> reservations;

    // Un tour puede contener muchos tickets
    @OneToMany(
        mappedBy = "tour",
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER, // Trae todo el join
        orphanRemoval = true // Eliminar los datos huerffanos
    )
    private Set<TicketEntity> tickets;

    // Muchos customers pueden tener un tour y no ser nullable
    @ManyToOne
    @JoinColumn(name = "id_customer")
    private CustomerEntity customer;

}
