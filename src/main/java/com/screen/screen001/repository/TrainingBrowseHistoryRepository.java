package com.screen.screen001.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.screen.screen001.dto.TrainingBrowseHistory;

public interface TrainingBrowseHistoryRepository extends JpaRepository<TrainingBrowseHistory, Integer>{
    
    List<TrainingBrowseHistory> findByMemberIdAndTrainingId(String memberId, String trainingId);

    List<TrainingBrowseHistory> findByMemberId(String memberId);

    List<TrainingBrowseHistory> findAll();
    
}
