package com.screen.screen001.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.screen.screen001.dto.TrainingBrowseHistory;

public interface TrainingBrowseHistoryRepository extends JpaRepository<TrainingBrowseHistory, Integer>{
    
    Iterable<TrainingBrowseHistory> findByMemberId(String memberId);

    Optional<TrainingBrowseHistory> findByTrainingId(String trainingId);

    List<TrainingBrowseHistory> findAll();
    
}
