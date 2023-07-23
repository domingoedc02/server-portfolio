package com.screen.screen001.services;


import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.screen.screen001.dto.TrainingTopics;
import com.screen.screen001.entity.Role;
import com.screen.screen001.entity.User;
import com.screen.screen001.repository.TrainingTopicsRepository;
import com.screen.screen001.repository.UserRepository;

import jakarta.mail.internet.MimeMessage;

@Service
public class AdminService {
    
    @Autowired
    private TrainingTopicsRepository topicsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JavaMailSender javaMailSender;


    public void sendEmail(
        String toEmail,
        String subject,
        String body
    ){
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, "utf-8");
        try{
        messageHelper.setFrom("noreply.screen001@gmail.com", "株式会社SCREEN");
        messageHelper.setTo(toEmail);
        messageHelper.setText(body, true);
        messageHelper.setSubject(subject);
        } catch(Exception e){

        }

        javaMailSender.send(message);
    }

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

    public void saveUser(User user, String inputUser){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        User users = User
            .builder()
            .memberId(user.getMemberId())
            .authority(Role.USER)
            .deleteFlag("0")
            .email(user.getEmail())
            .memberName(user.getMemberName())
            .password(passwordEncoder.encode(user.getPassword()))
            .insertMember(inputUser)
            .insertDate(timestamp)
            .updateMember(inputUser)
            .updateDate(timestamp)
            .build();
        
        if(user.getMemberId() != null && user.getPassword() != null){
            String bodyTemplate = "<h5> Member ID: "+user.getMemberId()+"</h5> <h5> Password: "+user.getPassword()+"</h5>";
            sendEmail(user.getEmail(), "WELCOME TO SCREEN", bodyTemplate);
        }
        userRepository.save(users);
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
            if(data.getDeleteFlag().equals("0")){
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
            }
            
        });

        return topics;
    }

    public List<User> getAllUsers(){
        Iterable<User> findUser = userRepository.findAll();
        List<User> users = new ArrayList<>();

        findUser.forEach(user -> {
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
        
        return users;

    }

    // public TrainingTopics deleteTrainingTopic(String trainingID, String updateMember) throws UsernameNotFoundException{
    //     Iterable<TrainingTopics> topic = topicsRepository.findByTrainingId(trainingID);
    //     // TrainingTopics training = (TrainingTopics) topicsRepository.findByTrainingId(trainingID);
        
    //     // Timestamp timestamp = new Timestamp(0);
    //     // training.setDeleteFlag("1");
    //     // training.setUpdateMember(updateMember);
    //     // training.setUpdateDate(timestamp);

        
        
    // }

    
}
