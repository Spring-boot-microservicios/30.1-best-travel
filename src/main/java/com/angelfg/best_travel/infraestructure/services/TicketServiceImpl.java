package com.angelfg.best_travel.infraestructure.services;

import com.angelfg.best_travel.api.dtos.request.TicketRequest;
import com.angelfg.best_travel.api.dtos.response.FlyResponse;
import com.angelfg.best_travel.api.dtos.response.TicketResponse;
import com.angelfg.best_travel.domain.entities.TicketEntity;
import com.angelfg.best_travel.domain.repositories.jpa.CustomerRepository;
import com.angelfg.best_travel.domain.repositories.jpa.FlyRepository;
import com.angelfg.best_travel.domain.repositories.jpa.TicketRepository;
import com.angelfg.best_travel.infraestructure.abstract_services.TicketService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;

    @Override
    public TicketResponse create(TicketRequest request) {
        return null;
    }

    @Override
    public TicketResponse read(UUID uuid) {
        return null;
    }

    @Override
    public TicketResponse update(TicketRequest request, UUID uuid) {
        return null;
    }

    @Override
    public void delete(UUID uuid) {

    }

    // Realizar el mapeo entre entities a dtos
    private TicketResponse entityToResponse(TicketEntity entity) {
        TicketResponse ticketResponse = new TicketResponse();
        BeanUtils.copyProperties(entity, ticketResponse); // Copia las propiedades semejantes a la response

        FlyResponse flyResponse = new FlyResponse();
        BeanUtils.copyProperties(entity.getFly(), flyResponse);

        ticketResponse.setFly(flyResponse);

        return  ticketResponse;
    }

}
