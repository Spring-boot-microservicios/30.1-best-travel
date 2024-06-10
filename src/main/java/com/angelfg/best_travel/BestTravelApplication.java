package com.angelfg.best_travel;

import com.angelfg.best_travel.domain.entities.*;
import com.angelfg.best_travel.domain.repositories.jpa.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
//		hotelRepository.findByRatingGreaterThan(3).forEach(System.out::println);

//		JOIN en lenguaje inclusivo de spring JPA
//		HotelEntity hotelEntity = hotelRepository.findByReservationsId(UUID.fromString("12345678-1234-5678-1234-567812345678")).get();
//		System.out.println(hotelEntity);

//		addTourComplete();

	}

	private void addTourComplete() throws InterruptedException {
		CustomerEntity customer = customerRepository.findById("GOTW771012HMRGR087").orElseThrow();
		log.info("Client name: " + customer.getFullName());

		FlyEntity fly = flyRepository.findById(11L).orElseThrow();
		log.info("Fly: " + fly.getOriginName() + " - " + fly.getDestinyName());

		HotelEntity hotel = hotelRepository.findById(3L).orElseThrow();
		log.info("Hotel: " + hotel.getName());

		TourEntity tour = TourEntity.builder()
				.customer(customer)
				.build();

		log.info("Tour: " + tour);

		TicketEntity ticket = TicketEntity.builder()
				.id(UUID.randomUUID())
				.price(fly.getPrice().multiply(BigDecimal.TEN))
				.arrivalDate(LocalDateTime.now())
				.departureDate(LocalDateTime.now())
				.purchaseDate(LocalDate.now())
				.customer(customer)
				.tour(tour)
				.fly(fly)
				.build();

		log.info("ticket: " + ticket);

		ReservationEntity reservation = ReservationEntity.builder()
				.id(UUID.randomUUID())
				.dateTimeReservation(LocalDateTime.now())
				.dateStart(LocalDate.now().plusDays(1))
				.dateEnd(LocalDate.now().plusDays(2))
				.hotel(hotel)
				.customer(customer)
				.tour(tour)
				.totalDays(1)
				.price(hotel.getPrice().multiply(BigDecimal.TEN))
				.build();

		log.info("reservation: " + reservation);

		System.out.println("-------SAVING-------");

		// Genera las relaciones para realizar el guardado correctamente
		tour.addReservation(reservation);
		tour.updateReservation();

		tour.addTicket(ticket);
		tour.updateTicket();

		TourEntity tourSaved = this.tourRepository.save(tour);
		System.out.println("Tour generado: " + tourSaved);

		Thread.sleep(8000);

		// No elimina por que el fetch es de tipo EAGER => cambiar a LAZY
		this.tourRepository.deleteById(tourSaved.getId());

		/**
		 * PRUEBAS EN DB DEL SAVE
		 * select * from tour;
		 *
		 * truncate table tour cascade; // Limpia en cascada las relaciones que contiene
		 *
		 * select * from tour t
		 * 	join reservation r on t.id = r.tour_id
		 * 	join hotel h on h.id = r.hotel_id
		 * 	join customer c on c.dni = r.customer_id;
		 */
	}

}
