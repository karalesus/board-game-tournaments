package com.example.tournaments;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication(scanBasePackages = {"com.example.tournaments", "com.example.tournamentsapi", "com.example.tournaments.graphql", "org.example"})
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class BoardGameTournamentsGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardGameTournamentsGatewayApplication.class, args);
	}

}
