package com.example.SSU_Rental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SsuRentalApplication {

//	static {
//		System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
//	}

	public static void main(String[] args) {
		SpringApplication.run(SsuRentalApplication.class, args);
	}


}
