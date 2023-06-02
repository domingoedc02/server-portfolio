package com.screen.screen001.controller;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import com.screen.screen001.dto.TrainingTopics;
import com.screen.screen001.entity.User;
import com.screen.screen001.services.AdminService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/screen001")
public class UserController {

    
    private UserDetails userDetails;

    @Autowired
    private AdminService adminService;

    @GetMapping("/menu")

    String menu(Model model) {
        List<TrainingTopics> topics = adminService.getAllTrainingTopics();
        System.out.println(topics);
        
        model.addAttribute("listOfTopics", topics);
        return "menu";
    }

    @GetMapping("/trainingboard/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String trainingBoard(Model model, HttpServletRequest request) {
        String url = request.getRequestURI();
        String temp[] = url.split("/trainingboard/");
        String id = temp[1];

        List<TrainingTopics> topics = adminService.getTopicsById(id);
        System.out.println("HELLLOOOOO "+topics);

        model.addAttribute("topic", topics);



        return "trainingBoard";
    }

    @GetMapping("/trainingboard/{id}/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String trainingBoardEdit(Model model) {
        return "trainingBoardEdit";
    }

    @GetMapping("/memberlist")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    String memberList(Model model, HttpServletRequest request) {
        List<User> users = adminService.getAllUsers();
        model.addAttribute("users", users);
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
