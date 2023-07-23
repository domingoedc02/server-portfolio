package com.screen.screen001.services;


import com.screen.screen001.auth.AuthenticationRequest;
import com.screen.screen001.auth.AuthenticationResponse;
import com.screen.screen001.auth.RegisterRequest;
import com.screen.screen001.entity.MemberProfile;
import com.screen.screen001.entity.Role;
import com.screen.screen001.entity.User;
import com.screen.screen001.repository.MemberProfileRepository;

import com.screen.screen001.repository.UserRepository;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final MemberProfileRepository profileRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;

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
  



  public AuthenticationResponse register(RegisterRequest request) {
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    var user = User.builder()
    .memberName(request.getMemberName())
    .memberId(request.getMemberId())
    .email(request.getEmail())
    .password(passwordEncoder.encode(request.getPassword()))
    .authority(Role.USER)
    .deleteFlag("0")
    .insertMember(request.getInsertMember())
    .insertDate(timestamp)
    .updateMember(request.getUpdateMember())
    .updateDate(timestamp)

        .build();
    var savedUser = repository.save(user);

    var userProfile = MemberProfile.builder()
      .memberId(request.getMemberId())
      .memberName(request.getMemberName())
      .insertUser(request.getInsertMember())
      .insertDate(timestamp)
      .updateUser(request.getUpdateMember())
      .updateDate(timestamp)
      .build();
    profileRepository.save(userProfile);

    //HTML template of body
    String bodyTemplate = "<h5> Member ID: "+request.getMemberId()+"</h5> <h5> Password: "+request.getPassword()+"</h5>";

    sendEmail(request.getEmail(), "WELCOME TO SCREEN", bodyTemplate);


    return AuthenticationResponse.builder()

        .build();
    }


  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    
    authenticationManager.authenticate(
        
        new UsernamePasswordAuthenticationToken(
            request.getMemberId(),
            request.getPassword()
        )
    );

    // var user = repository.findByMemberId(request.getMemberId())
        // .orElseThrow();


    return AuthenticationResponse.builder()

        .build();
  }}
