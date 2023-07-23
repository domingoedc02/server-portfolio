package com.screen.screen001.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.screen.screen001.dto.TrainingTopics;


public interface TrainingControllsRepository extends CrudRepository<TrainingTopics, Integer>{
    
    Optional<TrainingTopics> findByTrainingId(String trainingId);
    // Optional<TrainingTopics> findAll(String trainingId);
   
}
