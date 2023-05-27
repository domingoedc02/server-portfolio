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

	@Autowired
	private TrainingTopicsRepository repository;

	public void run(String... args) throws Exception{
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Time time = new Time(System.currentTimeMillis());
        TrainingTopics trainingTopics = new TrainingTopics();
        

        trainingTopics.setTrainingId("SR001");
        trainingTopics.setTrainingName("Test");
        trainingTopics.setTrainingDate(timestamp);
        trainingTopics.setTrainingStartTime(time);
        trainingTopics.setTrainingEndTime(time);
        trainingTopics.setTrainingDetails("test training details");
        trainingTopics.setInsertMember("SR001");
        trainingTopics.setInsertDate(timestamp);
        trainingTopics.setUpdateMember("SR001");
        trainingTopics.setUpdateDate(timestamp);
        repository.save(trainingTopics);
		System.out.println(trainingTopics);
		
	}

}
