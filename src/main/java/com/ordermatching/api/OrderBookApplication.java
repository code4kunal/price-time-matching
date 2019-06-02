package com.ordermatching.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.coindcx"})
@EnableJpaRepositories(basePackages = {"com.coindcx.api.persistence.dao"})
public class OrderBookApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderBookApplication.class, args);
	}

}
