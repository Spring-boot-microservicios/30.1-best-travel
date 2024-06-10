package com.angelfg.best_travel;

import com.angelfg.best_travel.domain.entities.*;
import com.angelfg.best_travel.domain.repositories.jpa.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@SpringBootApplication
@Slf4j
@AllArgsConstructor
public class BestTravelApplication implements CommandLineRunner {

	private final HotelRepository hotelRepository;
	private final FlyRepository flyRepository;
	private final TicketRepository ticketRepository;
	private final ReservationRepository reservationRepository;
	private final TourRepository tourRepository;
	private final CustomerRepository customerRepository;

	public static void main(String[] args) {
		SpringApplication.run(BestTravelApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		FlyEntity fly = flyRepository.findById(15L).get();
//		HotelEntity hotel = hotelRepository.findById(7L).get();
//		TicketEntity ticket = ticketRepository.findById(UUID.fromString("22345678-1234-5678-3235-567812345678")).get();
//		ReservationEntity reservation = reservationRepository.findById(UUID.fromString("32345678-1234-5678-1234-567812345678")).get();
//		CustomerEntity customer= customerRepository.findById("BBMB771012HMCRR022").get();
//
//		log.info(String.valueOf(fly));
//		log.info(String.valueOf(hotel));
//		log.info(String.valueOf(ticket));
//		log.info(String.valueOf(reservation));
//		log.info(String.valueOf(customer));

//		JPQL
//		this.flyRepository.selectLessPrice(BigDecimal.valueOf(20)).forEach(System.out::println);
//		System.out.println("---------------------------");
//		this.flyRepository.selectBetweenPrice(BigDecimal.valueOf(10), BigDecimal.valueOf(15)).forEach(System.out::println);
//		System.out.println("---------------------------");
//		this.flyRepository.selectOriginDestiny("Grecia", "Mexico").forEach(System.out::println);

//		FlyEntity fly = flyRepository.findById(1L).get();
//		log.info(String.valueOf(fly));
//		fly.getTickets().stream().forEach(System.out::println);

//		JOIN FETCH -> para relacion entre tablas
//		FlyEntity fly = flyRepository.findByTicketId(UUID.fromString("12345678-1234-5678-2236-567812345678")).get();
//		System.out.println(fly);

//		JPQL palabras propias para realizar queries
//		Lenguaje inclusivo de spring JPQL
//		hotelRepository.findByPriceLessThan(BigDecimal.valueOf(100)).forEach(System.out::println);
//		hotelRepository.findByPriceIsBetween(BigDecimal.valueOf(100), BigDecimal.valueOf(200)).forEach(System.out::println);
		hotelRepository.findByRatingGreaterThan(3).forEach(System.out::println);

	}

}
