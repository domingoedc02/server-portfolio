package com.screen.screen001.controller;

import java.nio.charset.Charset;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.screen.screen001.dto.TrainingTopics;
import com.screen.screen001.entity.User;
import com.screen.screen001.repository.TrainingTopicsRepository;
import com.screen.screen001.services.AdminService;


@Controller
@RequestMapping("/screen001")
@CrossOrigin
public class AdminController {

    
    private TrainingTopicsRepository trainingTopicsRepository;

    @Autowired
    private AdminService adminService;
    

    @GetMapping("/adminmenu")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    String adminMenu(Model model) {
        List<TrainingTopics> topics = adminService.getAllTrainingTopics();
        List<User> users = adminService.getAllUsers();
        // System.out.println(topics);
        model.addAttribute("listOfTopics", topics);
        model.addAttribute("userList",users);
        return "admin";
    }

    @GetMapping("/trainingadd")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    String addTraining(Model model) {
        TrainingTopics topics = new TrainingTopics();
        model.addAttribute("topics", topics);
        return "addTraining";
    }

    @PostMapping("/form/trainingadd")
    public ResponseEntity<TrainingTopics> addNewTraining(
        @RequestBody TrainingTopics trainingTopics, Authentication authentication, @ModelAttribute("") TrainingTopics tTopic
    ) throws Exception{
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        //Generate training_id
        Random rand = new Random(); 
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Character code = letters.charAt(rand.nextInt(10));
        int id1 = rand.nextInt(10); 
        int id2 = rand.nextInt(10); 
        int id3 = rand.nextInt(10); 
        String id = "T" + String.valueOf(code) + String.valueOf(id1) + String.valueOf(id2) + String.valueOf(id3);


        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        trainingTopics.setDeleteFlag("0");
        System.out.println("form "+trainingTopics);
        trainingTopics.setTrainingId(id);
        trainingTopics.setInsertMember(userDetails.getUsername());;
        trainingTopics.setInsertDate(timestamp);
        trainingTopics.setUpdateMember(userDetails.getUsername());
        trainingTopics.setUpdateDate(timestamp);

        adminService.saveTrainingTopics(trainingTopics);
        return new ResponseEntity<>(trainingTopics, HttpStatus.CREATED);
    }

    @GetMapping("/memberadd")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    String addMember(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        model.addAttribute("adminId", userDetails.getUsername());

        User user = new User();
        model.addAttribute("user", user);
        //Generate Password
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 7;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int) 
            (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
        

        model.addAttribute("temporaryPassword", generatedString);
        return "addMember";
    }

}
