package com.angelfg.best_travel;

import com.angelfg.best_travel.domain.entities.FlyEntity;
import com.angelfg.best_travel.domain.entities.HotelEntity;
import com.angelfg.best_travel.domain.repositories.jpa.FlyRepository;
import com.angelfg.best_travel.domain.repositories.jpa.HotelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class BestTravelApplication implements CommandLineRunner {

	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	private FlyRepository flyRepository;

	public static void main(String[] args) {
		SpringApplication.run(BestTravelApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		FlyEntity fly = flyRepository.findById(15L).get();
		HotelEntity hotel = hotelRepository.findById(7L).get();

		log.info(String.valueOf(fly));
		log.info(String.valueOf(hotel));
	}

}
