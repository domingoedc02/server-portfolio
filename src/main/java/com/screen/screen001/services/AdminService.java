package com.screen.screen001.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.screen.screen001.dto.TrainingTopics;
import com.screen.screen001.entity.User;
import com.screen.screen001.repository.TrainingTopicsRepository;
import com.screen.screen001.repository.UserRepository;

@Service
public class AdminService {
    
    @Autowired
    private TrainingTopicsRepository topicsRepository;

    @Autowired
    private UserRepository userRepository;

    public void saveTrainingTopics(TrainingTopics topic){
        // topic.builder().build();
        TrainingTopics topics = TrainingTopics
            .builder()
            .trainingId(topic.getTrainingId())
            .trainingName(topic.getTrainingName())
            .trainingDate(topic.getTrainingDate())
            .trainingStartTime(topic.getTrainingStartTime())
            .trainingEndTime(topic.getTrainingEndTime())
            .trainingDetails(topic.getTrainingDetails())
            .insertMember(topic.getInsertMember())
            .insertDate(topic.getInsertDate())
            .deleteFlag(topic.getDeleteFlag())
            .updateMember(topic.getUpdateMember())
            .updateDate(topic.getUpdateDate())
            .build();
            
        topicsRepository.save(topics);
    }

    public List<TrainingTopics> getAllTrainingTopics(){
        Iterable<TrainingTopics> findTopic = topicsRepository.findAll();
        List<TrainingTopics> topics = new ArrayList<>();

        findTopic.forEach(data -> {
            if(data.getDeleteFlag().equals("0")){
                topics.add(TrainingTopics
                    .builder()
                    .trainingId(data.getTrainingId())
                    .trainingName(data.getTrainingName())
                    .trainingDate(data.getTrainingDate())
                    .deleteFlag(data.getDeleteFlag())
                    .trainingStartTime(data.getTrainingStartTime())
                    .trainingEndTime(data.getTrainingEndTime())
                    .trainingDetails(data.getTrainingDetails())
                    .insertMember(data.getInsertMember())
                    .insertDate(data.getInsertDate())
                    .updateMember(data.getUpdateMember())
                    .updateDate(data.getUpdateDate())
                    .build()
                );
            }
        });

        Collections.reverse(topics);

        return topics;
    }

    public List<TrainingTopics> getTopicsById(String trainingId){
        Iterable<TrainingTopics> findTopic = topicsRepository.findByTrainingId(trainingId);

        List<TrainingTopics> topics = new ArrayList<>();

        findTopic.forEach(data -> {
            
            topics.add(TrainingTopics
                .builder()
                .trainingId(data.getTrainingId())
                .trainingName(data.getTrainingName())
                .trainingDate(data.getTrainingDate())
                .trainingStartTime(data.getTrainingStartTime())
                .trainingEndTime(data.getTrainingEndTime())
                .trainingDetails(data.getTrainingDetails())
                .insertMember(data.getInsertMember())
                .insertDate(data.getInsertDate())
                .updateMember(data.getUpdateMember())
                .updateDate(data.getUpdateDate())
                .build()
            );
        });

        return topics;
    }

    public List<User> getAllUsers(){
        Iterable<User> findUser = userRepository.findAll();
        List<User> users = new ArrayList<>();

        findUser.forEach(user -> {
            System.out.println(user.getDeleteFlag().equals("0"));
            if(user.getDeleteFlag().equals("0")){
                users.add(User
                    .builder()
                    .memberId(user.getMemberId())
                    .memberName(user.getMemberName())
                    .build()
                );
            }
            
        });

        Collections.reverse(users);
        System.out.println(users.get(0));
        return users;

    }

    
}
