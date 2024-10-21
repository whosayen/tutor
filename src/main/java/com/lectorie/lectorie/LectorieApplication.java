package com.lectorie.lectorie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LectorieApplication {
	public static void main(String[] args) {
		SpringApplication.run(LectorieApplication.class, args);
	}
}

