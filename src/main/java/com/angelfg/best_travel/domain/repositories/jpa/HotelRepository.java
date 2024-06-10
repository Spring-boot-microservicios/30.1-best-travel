package com.angelfg.best_travel.domain.repositories.jpa;

import com.angelfg.best_travel.domain.entities.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Set;

public interface HotelRepository extends JpaRepository<HotelEntity, Long> {

    // Busqueda con el precio mas bajo que..
    Set<HotelEntity> findByPriceLessThan(BigDecimal price);

    // Trae hoteles que esten entre el precio minimo y el maximo
    Set<HotelEntity> findByPriceIsBetween(BigDecimal min, BigDecimal max);

    // Trae los hoteles con raiting mator a..
    Set<HotelEntity> findByRatingGreaterThan(Integer rating);

}
