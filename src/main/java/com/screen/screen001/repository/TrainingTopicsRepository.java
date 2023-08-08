package com.screen.screen001.repository;



import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.screen.screen001.dto.TrainingTopics;

public interface TrainingTopicsRepository extends CrudRepository<TrainingTopics, Integer>{
    List<TrainingTopics> findAll();

    Iterable<TrainingTopics> findByTrainingId(String trainingId);

    // List<TrainingTopics> findByTrainingId(String trainingId);(String trainingId);
}
