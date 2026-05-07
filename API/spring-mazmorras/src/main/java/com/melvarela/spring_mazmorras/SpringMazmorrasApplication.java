package com.melvarela.spring_mazmorras;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@SpringBootApplication
public class SpringMazmorrasApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringMazmorrasApplication.class, args);

		System.out.println("\nAPI is up and running!\n");
	}

}
