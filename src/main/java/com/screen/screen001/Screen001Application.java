package com.screen.screen001;

import java.sql.Time;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.screen.screen001.dto.TrainingTopics;
import com.screen.screen001.repository.TrainingTopicsRepository;

@SpringBootApplication
public class Screen001Application {

	public static void main(String[] args) {
		SpringApplication.run(Screen001Application.class, args);
	}

	

}
