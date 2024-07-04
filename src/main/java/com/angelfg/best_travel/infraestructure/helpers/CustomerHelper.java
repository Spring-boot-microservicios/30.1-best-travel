package com.angelfg.best_travel.infraestructure.helpers;

import com.angelfg.best_travel.domain.entities.CustomerEntity;
import com.angelfg.best_travel.domain.repositories.jpa.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Transactional
@AllArgsConstructor
@Slf4j
public class CustomerHelper {

    private final CustomerRepository customerRepository;

    public void increase(String customerId, Class<?> type) {
        CustomerEntity customerToUpdate = this.customerRepository.findById(customerId).orElseThrow();

        switch (type.getSimpleName()) {
            case "TourServiceImpl" -> customerToUpdate.setTotalTours(customerToUpdate.getTotalTours() + 1);
            case "TicketServiceImpl" -> customerToUpdate.setTotalFlights(customerToUpdate.getTotalFlights() + 1);
            case "ReservationServiceImpl" -> customerToUpdate.setTotalLodgings(customerToUpdate.getTotalLodgings() + 1);
        }

        this.customerRepository.save(customerToUpdate);
    }

}
