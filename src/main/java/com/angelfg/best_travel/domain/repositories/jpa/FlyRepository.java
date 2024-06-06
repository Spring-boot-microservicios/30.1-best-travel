package com.angelfg.best_travel.domain.repositories.jpa;

import com.angelfg.best_travel.domain.entities.FlyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlyRepository extends JpaRepository<FlyEntity, Long> {

}
