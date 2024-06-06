package com.angelfg.best_travel.domain.repositories.jpa;

import com.angelfg.best_travel.domain.entities.TourEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourRepository extends JpaRepository<TourEntity, Long> {

}
