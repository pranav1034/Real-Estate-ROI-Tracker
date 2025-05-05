package com.cg.estate_tracker;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class EstateTrackerApplication {

	public static void main(String[] args) {

		SpringApplication.run(EstateTrackerApplication.class, args);
		log.info("Estate Tracker App Started in Environment");
	}

}
