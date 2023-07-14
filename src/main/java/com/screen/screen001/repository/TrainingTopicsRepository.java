package com.screen.screen001.repository;



import org.springframework.data.repository.CrudRepository;

import com.screen.screen001.dto.TrainingTopics;

public interface TrainingTopicsRepository extends CrudRepository<TrainingTopics, Integer>{
    Iterable<TrainingTopics> findAll();

    Iterable<TrainingTopics> findByTrainingId(String trainingId);

    // List<TrainingTopics> findByTrainingId(String trainingId);(String trainingId);
}