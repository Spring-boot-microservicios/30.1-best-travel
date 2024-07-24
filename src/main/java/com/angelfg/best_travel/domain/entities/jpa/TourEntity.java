package com.angelfg.best_travel.domain.entities.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

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
    @ToString.Exclude // Evita la recursividad infinita
    @EqualsAndHashCode.Exclude // Evita la recursividad infinita
    @OneToMany(
        mappedBy = "tour",
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER, // Trae todo el join
        orphanRemoval = true // Eliminar los datos huerffanos
    )
    private Set<ReservationEntity> reservations;

    // Un tour puede contener muchos tickets
    @ToString.Exclude // Evita la recursividad infinita
    @EqualsAndHashCode.Exclude // Evita la recursividad infinita
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

    // Ciclo de vida de una entity
    @PrePersist
    @PreRemove
    public void updateForeingKey() {
        this.tickets.forEach(ticket -> ticket.setTour(this));
        this.reservations.forEach(reservation -> reservation.setTour(this));
    }

    public void removeTicket(UUID id) {
        this.tickets.forEach(ticket -> {
            if (ticket.getId().equals(id)) {
                ticket.setTour(null);
            }
        });
    }

    public void addTicket(TicketEntity ticket) {
        if (Objects.isNull(this.tickets)) this.tickets = new HashSet<>();

        this.tickets.add(ticket);
        this.tickets.forEach(t -> t.setTour(this));
    }

    public void removeReservation(UUID id) {
        this.reservations.forEach(reservation -> {
            if (reservation.getId().equals(id)) {
                reservation.setTour(null);
            }
        });
    }

    public void addReservation(ReservationEntity reservation) {
        if (Objects.isNull(this.reservations)) this.reservations = new HashSet<>();

        this.reservations.add(reservation);
        this.reservations.forEach(r -> r.setTour(this));
    }

//    public void addTicket(TicketEntity ticket) {
//        if (Objects.isNull(this.tickets)) this.tickets = new HashSet<>();
//
//        this.tickets.add(ticket);
//    }
//
//    public void removeTicket(UUID id) {
//        if (Objects.isNull(this.tickets)) this.tickets = new HashSet<>();
//
//        this.tickets.removeIf(ticket -> ticket.getId().equals(id));
//    }
//
//    // Ciclos de vida de spring (deben ser void, no deben llevar parametros y no usar en varios metodos)
//    // @PreRemove // Se ejecuta antes de que sea eliminado
//    // @PreUpdate // Se ejecuta antes de que sea actualizada
//    // @PrePersist // Se ejecuta una vez que se quiera guardar en DB
//    public void updateTicket() {
//        // seteamos los datos en la entity
//        this.tickets.forEach(ticket -> ticket.setTour(this));
//    }
//
//    public void addReservation(ReservationEntity reservation) {
//        if (Objects.isNull(this.reservations)) this.reservations = new HashSet<>();
//
//        this.reservations.add(reservation);
//    }
//
//    public void removeReservation(UUID reservationId) {
//        if (Objects.isNull(this.reservations)) this.reservations = new HashSet<>();
//
//        this.reservations.removeIf(reservation -> reservation.getId().equals(reservationId));
//    }
//
//    public void updateReservation() {
//        // seteamos los datos en la entity
//        this.reservations.forEach(reservation -> reservation.setTour(this));
//    }

}
