package com.angelfg.best_travel.domain.repositories.jpa;

import com.angelfg.best_travel.domain.entities.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<HotelEntity, Long> {

}
