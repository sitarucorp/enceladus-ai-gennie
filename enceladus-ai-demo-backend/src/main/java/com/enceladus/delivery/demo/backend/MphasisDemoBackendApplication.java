package com.enceladus.delivery.demo.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication
public class MphasisDemoBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(MphasisDemoBackendApplication.class, args);
	}

}
