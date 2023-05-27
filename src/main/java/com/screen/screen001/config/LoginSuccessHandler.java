package com.screen.screen001.config;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@RequestMapping
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler{


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        String role = String.valueOf(userDetails.getAuthorities().toArray()[0]);
        String redirectURL = request.getContextPath();
        if(role.equals("ROLE_ADMIN")){
            redirectURL += "/screen001/adminmenu";
        } else if(role.equals("ROLE_USER")){
            redirectURL += "/screen001/menu";
        }

        response.sendRedirect(redirectURL);
    }
    
}
