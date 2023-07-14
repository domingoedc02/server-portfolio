package com.screen.screen001.services;

import com.screen.screen001.token.Token;
import com.screen.screen001.token.TokenType;
import com.screen.screen001.auth.AuthenticationRequest;
import com.screen.screen001.auth.AuthenticationResponse;
import com.screen.screen001.auth.RegisterRequest;
import com.screen.screen001.entity.Role;
import com.screen.screen001.entity.User;
import com.screen.screen001.repository.TokenRepository;
import com.screen.screen001.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
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

    //HTML template of body
    String bodyTemplate = "<h5> Member ID: "+request.getMemberId()+"</h5> <h5> Password: "+request.getPassword()+"</h5>";

    sendEmail(request.getEmail(), "WELCOME TO SCREEN", bodyTemplate);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
        .build();
  }


  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    
    authenticationManager.authenticate(
        
        new UsernamePasswordAuthenticationToken(
            request.getMemberId(),
            request.getPassword()
        )
    );

    var user = repository.findByMemberId(request.getMemberId())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getMemberId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByMemberId(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
