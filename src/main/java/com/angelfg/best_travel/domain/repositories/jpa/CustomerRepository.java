package com.angelfg.best_travel.domain.repositories.jpa;

import com.angelfg.best_travel.domain.entities.CustomerEntity;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<CustomerEntity, String> {

}
