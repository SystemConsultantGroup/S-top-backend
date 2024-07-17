package com.scg.stop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StopApplication {

	public static void main(String[] args) {
		SpringApplication.run(StopApplication.class, args);
	}

}
