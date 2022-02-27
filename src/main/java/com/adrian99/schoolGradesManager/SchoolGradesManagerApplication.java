package com.adrian99.schoolGradesManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SchoolGradesManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolGradesManagerApplication.class, args);
	}

}
