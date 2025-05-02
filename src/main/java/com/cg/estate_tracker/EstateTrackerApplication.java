package com.cg.estate_tracker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class EstateTrackerApplication {

	public static void main(String[] args) {

		SpringApplication.run(EstateTrackerApplication.class, args);
		log.info("Estate Tracker App Started in Environment");
	}

}
