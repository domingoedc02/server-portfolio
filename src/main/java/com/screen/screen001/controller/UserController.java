package com.screen.screen001.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/screen001")
public class UserController {

    
    private UserDetails userDetails;

    @GetMapping("/menu")
    @PreAuthorize("hasRole('ROLE_USER')")
    String menu(Model model) {
        
        return "menu";
    }

    @GetMapping("/trainingboard/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String trainingBoard(Model model) {
        return "trainingBoard";
    }

    @GetMapping("/trainingboard/{id}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String trainingBoardEdit(Model model) {
        return "trainingBoardEdit";
    }

    @GetMapping("/memberlist")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String memberList(Model model) {
        return "memberList";
    }

    @GetMapping("/memberlist/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String memberListId(Model model) {
        return "memberListId";
    }

    @GetMapping("/myprofile")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String myprofile(Model model) {
        return "myprofile";
    }

    @GetMapping("/myprofile/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String editMyprofile(Model model) {
        return "editMyprofile";
    }

}
