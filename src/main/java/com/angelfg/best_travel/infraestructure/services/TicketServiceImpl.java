package com.angelfg.best_travel.infraestructure.services;

import com.angelfg.best_travel.api.models.request.TicketRequest;
import com.angelfg.best_travel.api.models.response.FlyResponse;
import com.angelfg.best_travel.api.models.response.TicketResponse;
import com.angelfg.best_travel.domain.entities.CustomerEntity;
import com.angelfg.best_travel.domain.entities.FlyEntity;
import com.angelfg.best_travel.domain.entities.TicketEntity;
import com.angelfg.best_travel.domain.repositories.jpa.CustomerRepository;
import com.angelfg.best_travel.domain.repositories.jpa.FlyRepository;
import com.angelfg.best_travel.domain.repositories.jpa.TicketRepository;
import com.angelfg.best_travel.infraestructure.abstract_services.TicketService;
import com.angelfg.best_travel.infraestructure.helpers.BlackListHelper;
import com.angelfg.best_travel.infraestructure.helpers.CustomerHelper;
import com.angelfg.best_travel.util.BestTravelUtil;
import com.angelfg.best_travel.util.enums.Tables;
import com.angelfg.best_travel.util.exceptions.IdNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    public static final BigDecimal charger_price_percentage = BigDecimal.valueOf(0.25);

    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;

    @Override
    public TicketResponse create(TicketRequest request) {
        this.blackListHelper.isInBlackListCustomer(request.getIdClient());

        FlyEntity fly = this.flyRepository
                .findById(request.getIdFly())
                .orElseThrow(() -> new IdNotFoundException(Tables.fly.name()));

        CustomerEntity customer = this.customerRepository
                .findById(request.getIdClient())
                .orElseThrow(() -> new IdNotFoundException(Tables.customer.name()));

        TicketEntity ticketToPersist = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage)))
                .purchaseDate(LocalDate.now())
                .departureDate(BestTravelUtil.getRandomSoon())
                .arrivalDate(BestTravelUtil.getRandomLater())
                .build();

        TicketEntity ticketPersisted = this.ticketRepository.save(ticketToPersist);
        log.info("Ticket saved with id: {}", ticketPersisted.getId());

        this.customerHelper.increase(customer.getDni(), TicketServiceImpl.class);

        return this.entityToResponse(ticketPersisted);
    }

    @Override
    public TicketResponse read(UUID uuid) {
        TicketEntity ticketFromDB = this.ticketRepository
                .findById(uuid)
                .orElseThrow(() -> new IdNotFoundException(Tables.ticket.name()));

        return this.entityToResponse(ticketFromDB);
    }

    @Override
    public TicketResponse update(TicketRequest request, UUID id) {
        TicketEntity ticketToUpdate = this.ticketRepository.findById(id).orElseThrow(() -> new IdNotFoundException(Tables.ticket.name()));

        FlyEntity fly = this.flyRepository
                .findById(request.getIdFly())
                .orElseThrow(() -> new IdNotFoundException(Tables.fly.name()));

        ticketToUpdate.setFly(fly);
        ticketToUpdate.setPrice(fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage)));
        ticketToUpdate.setDepartureDate(BestTravelUtil.getRandomSoon());
        ticketToUpdate.setArrivalDate(BestTravelUtil.getRandomLater());

        TicketEntity ticketUpdated = this.ticketRepository.save(ticketToUpdate);

        log.info("Ticket updated with id {}", ticketUpdated.getId());

        return this.entityToResponse(ticketUpdated);
    }

    @Override
    public void delete(UUID id) {
        TicketEntity ticketToDelete = this.ticketRepository.findById(id).orElseThrow(() -> new IdNotFoundException(Tables.ticket.name()));
        this.ticketRepository.delete(ticketToDelete);
    }

    @Override
    public BigDecimal findPrice(Long flyId) {
        FlyEntity fly = this.flyRepository.findById(flyId).orElseThrow(() -> new IdNotFoundException(Tables.fly.name()));
        return fly.getPrice().add(fly.getPrice().multiply(charger_price_percentage)); // porcentaje
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
