package com.angelfg.best_travel.domain.entities.jpa;

import com.angelfg.best_travel.util.enums.AeroLine;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Entity(name = "fly")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FlyEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double originLat;
    private Double originLng;
    private Double destinyLat;
    private Double destinyLng;
    private BigDecimal price;

    @Column(length = 20)
    private String originName;

    @Column(length = 20)
    private String destinyName;

    @Enumerated(EnumType.STRING)
    private AeroLine aeroLine;

    // Un vuelo puede contener muchos tickets
    @ToString.Exclude // Evita la recursividad infinita
    @EqualsAndHashCode.Exclude // Evita la recursividad infinita
    @OneToMany(
        mappedBy = "fly",
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER, // Trae todo el join
        orphanRemoval = true // Eliminar los datos huerffanos
    )
    private Set<TicketEntity> tickets;

}
