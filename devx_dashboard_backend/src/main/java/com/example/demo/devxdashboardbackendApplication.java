package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * PUBLIC_INTERFACE
 * Spring Boot application entry point.
 * Scans both com.example.demo and com.example.devxdashboardbackend packages.
 */
@SpringBootApplication(scanBasePackages = {"com.example.demo", "com.example.devxdashboardbackend"})
public class devxdashboardbackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(devxdashboardbackendApplication.class, args);
	}

}
