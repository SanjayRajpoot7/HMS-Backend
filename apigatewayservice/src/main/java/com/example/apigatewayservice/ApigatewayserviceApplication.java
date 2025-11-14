package com.example.apigatewayservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApigatewayserviceApplication {

	private static final Logger logger = LoggerFactory.getLogger(ApigatewayserviceApplication.class);

	public static void main(String[] args) {
		logger.info("Starting API Gateway Service...");
		SpringApplication.run(ApigatewayserviceApplication.class, args);
		logger.info("API Gateway Service started successfully.");
	}
}
