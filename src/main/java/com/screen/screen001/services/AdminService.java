package com.screen.screen001.services;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.screen.screen001.dto.TrainingBrowseHistory;
import com.screen.screen001.dto.TrainingTopics;
import com.screen.screen001.entity.MemberProfile;
import com.screen.screen001.entity.Role;
import com.screen.screen001.entity.User;
import com.screen.screen001.repository.MemberProfileRepository;
import com.screen.screen001.repository.TrainingBrowseHistoryRepository;
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

    @Autowired
    private MemberProfileRepository memberProfileRepository;
    @Autowired
    private TrainingBrowseHistoryRepository browseHistoryRepository;
    
    private int count = 0;


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
        MemberProfile profile = MemberProfile
            .builder()
            .memberId(user.getMemberId())
            .memberName(user.getMemberName())
            .build();
        if(user.getMemberId() != null && user.getPassword() != null){
            String bodyTemplate = "<h5> Member ID: "+user.getMemberId()+"</h5> <h5> Password: "+user.getPassword()+"</h5>";
            sendEmail(user.getEmail(), "WELCOME TO SCREEN", bodyTemplate);
        }
        userRepository.save(users);
        memberProfileRepository.save(profile);
    }

    public List<TrainingTopics> getAllTrainingTopics(String memberId){
        List<TrainingTopics> findTopic = topicsRepository.findAll();
        List<TrainingBrowseHistory> browsed = browseHistoryRepository.findByMemberId(memberId);
        List<TrainingTopics> topics = new ArrayList<>();
        List<TrainingTopics> topicsTemp = new ArrayList<>();
        

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

        
        

        // if(browsed.isEmpty()){
        //     findTopic.forEach(data -> {
        //         if(data.getDeleteFlag().equals("0")){
        //             topics.add(TrainingTopics
        //                 .builder()
        //                 .trainingId(data.getTrainingId())
        //                 .trainingName(data.getTrainingName())
        //                 .trainingDate(data.getTrainingDate())
        //                 .deleteFlag(data.getDeleteFlag())
        //                 .trainingStartTime(data.getTrainingStartTime())
        //                 .trainingEndTime(data.getTrainingEndTime())
        //                 .trainingDetails(data.getTrainingDetails())
        //                 .insertMember(data.getInsertMember())
        //                 .insertDate(data.getInsertDate())
        //                 .updateMember(data.getUpdateMember())
        //                 .updateDate(data.getUpdateDate())
        //                 .build()
        //             );
        //         }
        //     });
        //     Collections.sort(topics, new Comparator<TrainingTopics>() {
        //         public int compare(TrainingTopics t1, TrainingTopics t2){
        //             return Long.valueOf(t1.getTrainingDate().getTime()).compareTo(t2.getTrainingDate().getTime());
        //         }
        //     });
        //     Collections.reverse(topics);
        // } else {
        //     findTopic.forEach(data -> {
        //         if(browsed.size() >= count){
        //             browsed.forEach(userBrowsed -> {
        //                 if (data.getTrainingId().equals(userBrowsed.getTrainingId()) && data.getDeleteFlag().equals("0")) {
        //                     topics.add(TrainingTopics
        //                         .builder()
        //                         .trainingId(data.getTrainingId())
        //                         .trainingName(data.getTrainingName())
        //                         .trainingDate(data.getTrainingDate())
        //                         .deleteFlag(data.getDeleteFlag())
        //                         .trainingStartTime(data.getTrainingStartTime())
        //                         .trainingEndTime(data.getTrainingEndTime())
        //                         .insertMember(data.getInsertMember())
        //                         .insertDate(data.getInsertDate())
        //                         .updateMember(data.getUpdateMember())
        //                         .updateDate(data.getUpdateDate())
        //                         .build()
        //                     );
        //                 } 
        //             });
        //         } 
            
        //         count++;

        //     });
        //     Collections.sort(topics, new Comparator<TrainingTopics>() {
        //         public int compare(TrainingTopics t1, TrainingTopics t2){
        //             return Long.valueOf(t1.getTrainingDate().getTime()).compareTo(t2.getTrainingDate().getTime());
        //         }
        //     });
            
            // findTopic.forEach(data -> {
            //     if(browsed.size() < count){
            //         browsed.forEach(userBrowsed -> {
            //             if (!(data.getTrainingId().equals(userBrowsed.getTrainingId())) && data.getDeleteFlag().equals("0")) {
            //                 topicsTemp.add(TrainingTopics
            //                     .builder()
            //                     .trainingId(data.getTrainingId())
            //                     .trainingName(data.getTrainingName())
            //                     .trainingDate(data.getTrainingDate())
            //                     .deleteFlag(data.getDeleteFlag())
            //                     .trainingStartTime(data.getTrainingStartTime())
            //                     .trainingEndTime(data.getTrainingEndTime())
            //                     .insertMember(data.getInsertMember())
            //                     .insertDate(data.getInsertDate())
            //                     .updateMember(data.getUpdateMember())
            //                     .updateDate(data.getUpdateDate())
            //                     .build()
            //                 );
            //             } 
            //         });
            //     } 
            
            //     count++;

            // });
            

            // if(!topicsTemp.isEmpty()){
            //     topicsTemp.forEach(data -> {
            //         topics.add(TrainingTopics
            //                 .builder()
            //                 .trainingId(data.getTrainingId())
            //                 .trainingName(data.getTrainingName())
            //                 .trainingDate(data.getTrainingDate())
            //                 .deleteFlag(data.getDeleteFlag())
            //                 .trainingStartTime(data.getTrainingStartTime())
            //                 .trainingEndTime(data.getTrainingEndTime())
            //                 .insertMember(data.getInsertMember())
            //                 .insertDate(data.getInsertDate())
            //                 .updateMember(data.getUpdateMember())
            //                 .updateDate(data.getUpdateDate())
            //                 .build()
            //         );
            //     });
            // }

            // HashSet hash = new HashSet<>(topics);
            // List<TrainingTopics> temp = new ArrayList<TrainingTopics>(hash);
            // topics.clear();
            // topics.addAll(temp);

            // for(int i = 0; i < findTopic.size(); i++){
            //     for(int j = 0; j < browsed.size(); j++){
            //         if(!(findTopic.get(i).getTrainingId().equals(browsed.get(j).getTrainingId())) && findTopic.get(i).getDeleteFlag().equals("0")){
            //             if(findTopic.size() > browsed.size()){
            //                 topics.add(TrainingTopics
            //                     .builder()
            //                     .trainingId(findTopic.get(i).getTrainingId())
            //                     .trainingName(findTopic.get(i).getTrainingName())
            //                     .trainingDate(findTopic.get(i).getTrainingDate())
            //                     .deleteFlag(findTopic.get(i).getDeleteFlag())
            //                     .trainingStartTime(findTopic.get(i).getTrainingStartTime())
            //                     .trainingEndTime(findTopic.get(i).getTrainingEndTime())
            //                     .insertMember(findTopic.get(i).getInsertMember())
            //                     .insertDate(findTopic.get(i).getInsertDate())
            //                     .updateMember(findTopic.get(i).getUpdateMember())
            //                     .updateDate(findTopic.get(i).getUpdateDate())
            //                     .build()
            //                 );
            //             }
                        
                        
            //         } else {
            //             topicsTemp.add(TrainingTopics
            //                 .builder()
            //                 .trainingId(findTopic.get(i).getTrainingId())
            //                 .trainingName(findTopic.get(i).getTrainingName())
            //                 .trainingDate(findTopic.get(i).getTrainingDate())
            //                 .deleteFlag(findTopic.get(i).getDeleteFlag())
            //                 .trainingStartTime(findTopic.get(i).getTrainingStartTime())
            //                 .trainingEndTime(findTopic.get(i).getTrainingEndTime())
            //                 .insertMember(findTopic.get(i).getInsertMember())
            //                 .insertDate(findTopic.get(i).getInsertDate())
            //                 .updateMember(findTopic.get(i).getUpdateMember())
            //                 .updateDate(findTopic.get(i).getUpdateDate())
            //                 .build()
            //             );
            //         }
            //     }
            // }
            
            

            // Collections.sort(topics, new Comparator<TrainingTopics>() {
            //     public int compare(TrainingTopics t1, TrainingTopics t2){
            //         return Long.valueOf(t1.getTrainingDate().getTime()).compareTo(t2.getTrainingDate().getTime());
            //     }
            // });
            // Collections.reverse(topics);
            
            // Collections.sort(topicsTemp, new Comparator<TrainingTopics>() {
            //     public int compare(TrainingTopics t1, TrainingTopics t2){
            //         return Long.valueOf(t1.getTrainingDate().getTime()).compareTo(t2.getTrainingDate().getTime());
            //     }
            // });

            // topicsTemp.forEach(data -> {
            //     topics.add(TrainingTopics
            //         .builder()
            //         .trainingId(data.getTrainingId())
            //         .trainingName(data.getTrainingName())
            //         .trainingDate(data.getTrainingDate())
            //         .trainingStartTime(data.getTrainingStartTime())
            //         .trainingEndTime(data.getTrainingEndTime())
            //         .trainingDetails(data.getTrainingDetails())
            //         .insertMember(data.getInsertMember())
            //         .insertDate(data.getInsertDate())
            //         .updateMember(data.getUpdateMember())
            //         .updateDate(data.getUpdateDate())
            //         .build()
            //     );
            // });

            // Collections.reverse(topics);
            
            
        

        // findTopic.forEach(data -> {
        //     if(data.getDeleteFlag().equals("0")){
        //         topics.add(TrainingTopics
        //             .builder()
        //             .trainingId(data.getTrainingId())
        //             .trainingName(data.getTrainingName())
        //             .trainingDate(data.getTrainingDate())
        //             .deleteFlag(data.getDeleteFlag())
        //             .trainingStartTime(data.getTrainingStartTime())
        //             .trainingEndTime(data.getTrainingEndTime())
        //             .trainingDetails(data.getTrainingDetails())
        //             .insertMember(data.getInsertMember())
        //             .insertDate(data.getInsertDate())
        //             .updateMember(data.getUpdateMember())
        //             .updateDate(data.getUpdateDate())
        //             .build()
        //         );
        //     }
        // });

        Collections.sort(topics, new Comparator<TrainingTopics>() {
                public int compare(TrainingTopics t1, TrainingTopics t2){
                    return Long.valueOf(t1.getTrainingDate().getTime()).compareTo(t2.getTrainingDate().getTime());
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
