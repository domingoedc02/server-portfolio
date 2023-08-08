package com.screen.screen001.controller;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.screen.screen001.dto.TrainingFiles;
import com.screen.screen001.dto.TrainingTopics;
import com.screen.screen001.entity.User;
import com.screen.screen001.repository.TrainingControllsRepository;
import com.screen.screen001.repository.TrainingFilesRepository;
import com.screen.screen001.repository.TrainingTopicsRepository;
import com.screen.screen001.repository.UserRepository;
import com.screen.screen001.services.AdminService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
@RequestMapping("/screen001")
@CrossOrigin
public class AdminController {

    public static String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/TrainingFiles";
    
    @Autowired
    private TrainingTopicsRepository trainingTopicsRepository;

    @Autowired
    private TrainingControllsRepository trainingControllerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private TrainingFilesRepository filesRepository;


    @GetMapping("/adminmenu")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    String adminMenu(Model model, Authentication autenticate) {
        UserDetails userDetails = (UserDetails) autenticate.getPrincipal();
        List<TrainingTopics> topics = adminService.getAllTrainingTopics(userDetails.getUsername());
        List<User> users = adminService.getAllUsers();

        
        
        model.addAttribute("listOfTopics", topics);
        model.addAttribute("userList",users);
        return "/admin";
    }

    @GetMapping("/trainingadd")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    String addTraining(Model model) {
        TrainingTopics topics = new TrainingTopics();
        model.addAttribute("topics", topics);
        return "/addTraining";
    }

    @PostMapping(value = "/form/trainingadd")
    public String addNewTraining(
        TrainingTopics trainingTopics, Authentication authentication, @ModelAttribute("") TrainingTopics tTopic, MultipartFile file
    ) throws Exception{
        
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        
        Iterable<TrainingTopics> findTopic = trainingTopicsRepository.findAll();
        ArrayList<Integer> training = new ArrayList<>();

        findTopic.forEach(ttopic -> {
                String[] id = (ttopic.getTrainingId()).split("TR");
                training.add(Integer.parseInt(id[1]));
        });
        int number = 0;
        if(training.size() == 0){
            number += 1;
        } else{
            number = Collections.max(training) + 1; 
        }

        String id = "TR" + String.format("%03d", number);

        Optional<TrainingTopics> newTraining = trainingControllerRepository.findByTrainingId(id);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();


        //File Upload
        
        try{
            if(!file.isEmpty()){

                // System.out.println(file.getOriginalFilename());
                
                StringBuilder fileNames = new StringBuilder();
                Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, id+file.getOriginalFilename());
                fileNames.append(id+file.getOriginalFilename());
                Files.write(fileNameAndPath, file.getBytes());
                TrainingFiles saveFiles = TrainingFiles
                    .builder()
                    .trainingId(id)
                    .uploadFile(file.getOriginalFilename())
                    .uploadFilePath(UPLOAD_DIRECTORY)
                    .insertUser(userDetails.getUsername())
                    .insertDate(timestamp)
                    .updateUser(userDetails.getUsername())
                    .updateDate(timestamp)
                    .contentType(file.getBytes())
                    .build();
                filesRepository.save(saveFiles);
            } else {
                System.out.println("none");
            }
            
        } catch (Exception e){
            System.out.println(e);
            return "redirect:/screen001/adminmenu?fileupload=failed";
        }

        if(!(newTraining.isPresent())){
            trainingTopics.setDeleteFlag("0");
        
            trainingTopics.setTrainingId(id);
            trainingTopics.setInsertMember(userDetails.getUsername());;
            trainingTopics.setInsertDate(timestamp);
            trainingTopics.setUpdateMember(userDetails.getUsername());
            trainingTopics.setUpdateDate(timestamp);

            adminService.saveTrainingTopics(trainingTopics);
            return "redirect:/screen001/adminmenu?trainingadd-successfull=true?trainingId="+id;
        } else{
            return "redirect:/screen001/adminmenu?trainingadd-successfull=false?message=trainingId is already used.";
        }
    }

    @GetMapping("/downloadfile")
    public void downloadFile(@Param("id") String id , Model model, HttpServletResponse response) throws IOException {
        Optional<TrainingFiles> temp = filesRepository.findByTrainingId(id);
        if(temp!=null) {
            TrainingFiles trainingFile = temp.get();
            // String[] arr = trainingFile.getUploadFile().;
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename = "+id+trainingFile.getUploadFile();
            response.setHeader(headerKey, headerValue);
            ServletOutputStream outputStream = response.getOutputStream();
            outputStream.write(trainingFile.getContentType());
            outputStream.close();
        }
    }

    @GetMapping("/memberadd")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    String addMember(Model model, Authentication authentication) {
        Iterable<User> findUser = userRepository.findAll();
        ArrayList<Integer> users = new ArrayList<>();
        findUser.forEach(user -> {
                String[] id = (user.getMemberId()).split("SC");
                users.add(Integer.parseInt(id[1]));
        });

        int number = 0;
        if(users.size() == 0){
            number += 1;
        } else{
            number = Collections.max(users) + 1; 
        }
        
        String id = "SC" + String.format("%03d", number); 

        User user = new User();
        
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
        
        user.setPassword(generatedString);
        user.setMemberId(id);
        model.addAttribute("user", user);

        model.addAttribute("temporaryPassword", generatedString);
        return "/addMember";
    }

    @PostMapping(path = "/form/memberadd", consumes = "application/x-www-form-urlencoded")
    String addNewMember(User user, Authentication authenticate, Model model){
        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
        
        Iterable<User> userAccount = userRepository.findAll();
        ArrayList<Integer> users = new ArrayList<>();
        userAccount.forEach(userProfile -> {
            String[] id = (userProfile.getMemberId()).split("SC");
            users.add(Integer.parseInt(id[1]));
        });
        int number = 0;
        if(users.size() == 0){
            number += 1;
        } else{
            number = Collections.max(users) + 1; 
        }

        String id = "SC" + String.format("%03d", number);

        Optional<User> optionalUser = userRepository.findByMemberId(user.getMemberId());
        if(!(optionalUser.isPresent())){
            try{
                adminService.saveUser(user, userDetails.getUsername());
                return "redirect:/screen001/adminmenu?user-added-successfull=true?userId="+id;
            } catch (Exception e){
                System.out.println(e);
                return "redirect:/screen001/adminmenu?user-added-successfull=false?message=database-error";
            }
        } else{
            return "redirect:/screen001/adminmenu?user-added-successfull=false?message=invalid-user";
        }

        
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

    // @PutMapping(path = "/training/update/{id}", consumes = "application/json")
    // // @PreAuthorize("hasRole('ROLE_ADMIN')")
    // public String updateTraining1(@PathVariable("id") String id , Authentication authentication, Model model, @RequestBody TrainingTopics training) throws Exception{
    //     Optional<TrainingTopics> topic = trainingControllerRepository.findByTrainingId(id);
    //     UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    //     Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    //     // Time startTime = Time.valueOf(String.valueOf(training.getTrainingStartTime() + ":00"));
    //     // Time endTime = Time.valueOf(String.valueOf(training.getTrainingEndTime() + ":00"));
    //     // System.out.println(startTime.getTime());
    //     if(topic.isPresent()){
    //         TrainingTopics trainingTopics = topic.get();
    //         trainingTopics.setTrainingName(training.getTrainingName());
    //         trainingTopics.setTrainingDate(training.getTrainingDate());
    //         // trainingTopics.setTrainingStartTime(startTime);
    //         // trainingTopics.setTrainingEndTime(endTime);
    //         trainingTopics.setTrainingDetails(training.getTrainingDetails());
    //         trainingTopics.setUpdateMember(userDetails.getUsername());
    //         trainingTopics.setUpdateDate(timestamp);
    //         trainingTopicsRepository.save(trainingTopics);
    //         // return new ResponseEntity<>(trainingTopicsRepository.save(trainingTopics), HttpStatus.OK);
    //         return "redirect:/screen001/trainingboard" + id;
    //     } else {
    //         // return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    //         return "redirect:/screen001/adminmenu?error=true";
    //       }

    // }

    @PutMapping(path = "/training/update/{id}")
    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateTraining2(@PathVariable("id") String id , Authentication authentication, Model model, TrainingTopics training, MultipartFile file){
        Optional<TrainingTopics> topic = trainingControllerRepository.findByTrainingId(id);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Optional<TrainingFiles> trainingFile = filesRepository.findByTrainingId(id);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String[] tempStartTime = String.valueOf(training.getTrainingStartTime()).split(":");
        String[] tempEndTime = String.valueOf(training.getTrainingEndTime()).split(":");
        System.out.println(tempStartTime.length);
        System.out.println(tempEndTime.length);
               
        if(topic.isPresent()){
            TrainingTopics trainingTopics = topic.get();
            trainingTopics.setTrainingName(training.getTrainingName());
            trainingTopics.setTrainingDate(training.getTrainingDate());
            trainingTopics.setTrainingStartTime(training.getTrainingStartTime());
            trainingTopics.setTrainingEndTime(training.getTrainingEndTime());
            trainingTopics.setTrainingDetails(training.getTrainingDetails());
            trainingTopics.setUpdateMember(userDetails.getUsername());
            trainingTopics.setUpdateDate(timestamp);
            trainingTopicsRepository.save(trainingTopics);
            try{
                if(!file.isEmpty()){
                    StringBuilder fileNames = new StringBuilder();
                    Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, id+file.getOriginalFilename());
                    fileNames.append(id+file.getOriginalFilename());
                    Files.write(fileNameAndPath, file.getBytes());
                    if(trainingFile.isPresent()){
                        TrainingFiles saveFiles = trainingFile.get();
                        saveFiles.setUploadFile(file.getOriginalFilename());
                        saveFiles.setUploadFilePath(UPLOAD_DIRECTORY);
                        saveFiles.setContentType(file.getBytes());
                        saveFiles.setUpdateUser(userDetails.getUsername());
                        saveFiles.setUpdateDate(timestamp);
                        filesRepository.save(saveFiles);
                    } else {
                        TrainingFiles saveFiles = TrainingFiles
                            .builder()
                            .trainingId(id)
                            .uploadFile(file.getOriginalFilename())
                            .uploadFilePath(UPLOAD_DIRECTORY)
                            .insertUser(userDetails.getUsername())
                            .insertDate(timestamp)
                            .updateUser(userDetails.getUsername())
                            .updateDate(timestamp)
                            .contentType(file.getBytes())
                            .build();
                        filesRepository.save(saveFiles);
                    }
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
                return "redirect:/screen001/adminmenu?uploading-file-error";
            }
            // return new ResponseEntity<>(trainingTopicsRepository.save(trainingTopics), HttpStatus.OK);
            return "redirect:/screen001/trainingboard/" + id + "?update-successfull=true";
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
