package com.screen.screen001.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.screen.screen001.dto.TrainingFiles;

public interface TrainingFilesRepository extends JpaRepository<TrainingFiles, Integer>{
    
    Optional<TrainingFiles> findByTrainingId(String trainingId);

}
