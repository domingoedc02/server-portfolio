package com.screen.screen001.controller;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.screen.screen001.dto.TrainingTopics;
import com.screen.screen001.entity.User;
import com.screen.screen001.repository.TrainingControllsRepository;
import com.screen.screen001.repository.TrainingTopicsRepository;
import com.screen.screen001.repository.UserRepository;
import com.screen.screen001.services.AdminService;


import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
@RequestMapping("/screen001")
@CrossOrigin
public class AdminController {

    @Autowired
    private TrainingTopicsRepository trainingTopicsRepository;

    @Autowired
    private TrainingControllsRepository trainingControllerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminService adminService;


    

    @GetMapping("/adminmenu")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    String adminMenu(Model model) {
        List<TrainingTopics> topics = adminService.getAllTrainingTopics();
        List<User> users = adminService.getAllUsers();
        
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

    @PostMapping(value = "/form/trainingadd")
    public ResponseEntity<TrainingTopics> addNewTraining(
        @RequestBody TrainingTopics trainingTopics, Authentication authentication, @ModelAttribute("") TrainingTopics tTopic
    ) throws Exception{
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        Iterable<TrainingTopics> findTopic = trainingTopicsRepository.findAll();
        ArrayList<Integer> training = new ArrayList<>();

        findTopic.forEach(ttopic -> {
            
            if(ttopic.getDeleteFlag().equals("0") && ttopic.getTrainingId().startsWith("TR")){
                String[] id = (ttopic.getTrainingId()).split("TR");

                training.add(Integer.parseInt(id[1]));
            }


            
        });
        int number = 0;
        if(training.size() == 0){
            number += 1;
        } else{
            number = Collections.max(training) + 1; 
        }

        String id = "TR" + String.format("%03d", number);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        trainingTopics.setDeleteFlag("0");
        
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
        Iterable<User> findUser = userRepository.findAll();
        ArrayList<Integer> users = new ArrayList<>();

        findUser.forEach(user -> {
            
            if(user.getDeleteFlag().equals("0") && user.getMemberId().startsWith("SC")){
                String[] id = (user.getMemberId()).split("SC");

                users.add(Integer.parseInt(id[1]));
            }
            
        });

        int number = 0;
        if(users.size() == 0){
            number += 1;
        } else{
            number = Collections.max(users) + 1; 
        }
        
        String id = "SC" + String.format("%03d", number); 
        model.addAttribute("memberIdNew", id);

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



    @PutMapping(value = "/training/delete/{id}")
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteTraining(@PathVariable("id") String id, Authentication authentication, Model model){
        Optional<TrainingTopics> topic = trainingControllerRepository.findByTrainingId(id);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if(topic.isPresent()){
            TrainingTopics trainingTopics = topic.get();
            trainingTopics.setDeleteFlag("1");
            trainingTopics.setUpdateMember(userDetails.getUsername());
            trainingTopics.setUpdateDate(timestamp);
            trainingTopicsRepository.save(trainingTopics);
            // return new ResponseEntity<>(trainingTopicsRepository.save(trainingTopics), HttpStatus.OK);
            return "redirect:/screen001/adminmenu";
        } else {
            // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return "redirect:/screen001/adminmenu?error=true";
          }

    }

    @PutMapping(value = "/training/update/{id}")
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateraining(@PathVariable("id") String id, Authentication authentication, Model model, @RequestBody RequestBody requestBody){
        Optional<TrainingTopics> topic = trainingControllerRepository.findByTrainingId(id);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if(topic.isPresent()){
            TrainingTopics trainingTopics = topic.get();
            
            trainingTopics.setUpdateMember(userDetails.getUsername());
            trainingTopics.setUpdateDate(timestamp);
            trainingTopicsRepository.save(trainingTopics);
            // return new ResponseEntity<>(trainingTopicsRepository.save(trainingTopics), HttpStatus.OK);
            return "redirect:/screen001/trainingboard/" + id + "/edit";
        } else {
            // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            return "redirect:/screen001/adminmenu?error=true";
          }

    }

    @PutMapping(value = "/user/delete/{id}")
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUser(@PathVariable("id") String id, Authentication authentication, Model model){
        Optional<User> user = userRepository.findByMemberId(id);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        if(user.isPresent()){
            User userDelete = user.get();
            userDelete.setDeleteFlag("1");
            userDelete.setUpdateMember(userDetails.getUsername());
            userDelete.setUpdateDate(timestamp);
            userRepository.save(userDelete);
            return "redirect:/screen001/adminmenu";
        } else {
            return "redirect:/screen001/adminmenu?error=true";
          }

    }

}
