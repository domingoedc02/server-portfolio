package com.screen.screen001.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.screen.screen001.auth.AuthenticationRequest;
import com.screen.screen001.auth.AuthenticationResponse;
import com.screen.screen001.auth.RegisterRequest;
import com.screen.screen001.services.AuthenticationService;

import java.io.IOException;

@Controller
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request
  ) {
    // System.out.println(new Gson().toJson(ResponseEntity.ok(service.register(request))));
    return ResponseEntity.ok(service.register(request));
  }
  
  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    
    return ResponseEntity.ok(service.authenticate(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }

  // @PostMapping("/logout")
  // public RedirectView logoutPage (HttpServletRequest request, 
  // HttpServletResponse response) {
  //     Authentication auth = 
  // SecurityContextHolder.getContext().getAuthentication();
  //     if (auth != null){
  //         new SecurityContextLogoutHandler().logout(request, response, auth);
  //     }
  //     return new RedirectView("/screen001/login");
  // } 

}
