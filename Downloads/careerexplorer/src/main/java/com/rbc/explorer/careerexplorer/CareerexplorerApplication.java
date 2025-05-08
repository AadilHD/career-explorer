package com.rbc.explorer.careerexplorer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CareerexplorerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CareerexplorerApplication.class, args);
	}

	@Bean
	CommandLineRunner seedJobs(JobRepository repo) {
		return args -> {
			if (repo.count() == 0) {
				repo.save(new Job("Full Stack Developer", "Toronto", new String[]{"Java", "React", "SQL"},
						"Develop and maintain full stack applications using Java, React, and SQL. Work closely with a team of developers to build robust APIs and user interfaces."));

				repo.save(new Job("API Developer", "Remote", new String[]{"Java", "Spring Boot", "REST"},
						"Design and develop scalable REST APIs using Spring Boot and Java. Collaborate with teams to integrate APIs into frontend and mobile apps."));

				repo.save(new Job("Data Analyst Intern", "Vancouver", new String[]{"Python", "Pandas", "SQL"},
						"Assist in data cleaning, transformation, and analysis using Python and Pandas. Generate reports and insights to support business decisions."));
			}
		};
	}
}