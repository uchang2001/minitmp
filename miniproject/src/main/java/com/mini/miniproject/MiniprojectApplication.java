package com.mini.miniproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//
//@EntityScan("com.mini.miniproject.model")
//@EnableJpaRepositories("com.mini.miniproject.repository")
@SpringBootApplication
public class MiniprojectApplication {

//	public static final String APPLICATION_LOCATIONS = "spring.config.location="
////			+ "classpath:application.yml,"
//			+ "classpath:aws.yml";

	public static void main(String[] args) {
//
//		new SpringApplicationBuilder(MiniprojectApplication.class)
//				.properties(APPLICATION_LOCATIONS)
//				.run(args);

		SpringApplication.run(MiniprojectApplication.class, args);
	}

}
