package com.screen.screen001.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.screen.screen001.entity.MemberProfile;
import com.screen.screen001.entity.User;
import com.screen.screen001.repository.MemberProfileRepository;
import com.screen.screen001.repository.UserControllerRepository;

import io.jsonwebtoken.Jwts;
import jakarta.mail.internet.MimeMessage;

@Service
public class UserService {
    
    @Autowired
    private MemberProfileRepository profileRepository;
    @Autowired
    private UserControllerRepository userRepository;
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

    public List<MemberProfile> getProfile(String id){
        Iterable<MemberProfile> members = profileRepository.findByMemberId(id);
        List<MemberProfile> profile = new ArrayList<>();
        Iterable<User> user = userRepository.findByMemberId(id);

        user.forEach(data -> {
            
            if(data.getDeleteFlag().equals("0")){
                members.forEach(userProfile -> {
                    profile.add(MemberProfile
                    .builder()
                    .memberId(userProfile.getMemberId())
                    .memberName(userProfile.getMemberName())
                    .birthDate(userProfile.getBirthDate())
                    .address1(userProfile.getAddress1())
                    .address2(userProfile.getAddress2())
                    .hobby(userProfile.getHobby())
                    .speciality(userProfile.getSpeciality())
                    .favoriteFood(userProfile.getFavoriteFood())
                    .hatedFood(userProfile.getHatedFood())
                    .goalsYear(userProfile.getGoalsYear())
                    .bloodType(userProfile.getBloodType())
                    .build()
                    );
                });
            }
        });

        
        
        return profile;
    }

    public void forgotPassword(String memberId, String email, String url){
        String token = Jwts.builder()
                            .claim("memberID", memberId)
                            .claim("email", email)
                            .compact();
        String bodyTemplate = "<a href='"+url+"?token="+token+"''>Click here</a>";
        sendEmail(email, "パスワードさい再設定再設定依頼", bodyTemplate);
    }
}
