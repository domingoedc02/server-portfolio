package com.screen.screen001.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/screen001")
public class LoginController {

    @GetMapping("/login")
    String login(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String redirectURL = "";
        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "/login";
        } else{
            String role = String.valueOf(authentication.getAuthorities().toArray()[0]);
            if(role.equals("ROLE_ADMIN")){
                redirectURL = "/adminmenu";
            } else if (role.equals("ROLE_USER")){ 
                redirectURL = "/menu";
            }
        }
        return "redirect:/screen001"+redirectURL;
    }

    @GetMapping("/changepassword")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String changePassword(Model model) {
        return "changePassword";
    }

    @GetMapping("/forgotpassword")
    String forgotPassword(Model model) {
        return "forgotPassword";
    }

    @GetMapping("/resetpassword")
    String resetPassword(Model model) {
        return "resetPassword";
    }

}
