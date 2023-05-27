package com.screen.screen001.controller;

import java.sql.Time;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.screen.screen001.dto.TrainingTopics;
import com.screen.screen001.repository.TrainingTopicsRepository;


@Controller
@RequestMapping("/screen001")

public class AdminController {

    
    private TrainingTopicsRepository trainingTopicsRepository;

    @GetMapping("/adminmenu")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    String adminMenu(Model model) {
        return "admin";
    }

    @GetMapping("/trainingadd")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    String addTraining(Model model) {
        return "addTraining";
    }

    @PostMapping("/api/trainingadd")
    public @ResponseBody String addNewTraining(
        @RequestParam String request
    ){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Time time = new Time(System.currentTimeMillis());
        TrainingTopics trainingTopics = new TrainingTopics();
        

        trainingTopics.setTrainingId(request);
        trainingTopics.setTrainingName("Test");
        trainingTopics.setTrainingDate(timestamp);
        trainingTopics.setTrainingStartTime(time);
        trainingTopics.setTrainingEndTime(time);
        trainingTopics.setTrainingDetails("test training details");
        trainingTopics.setInsertMember("SR001");
        trainingTopics.setInsertDate(timestamp);
        trainingTopics.setUpdateMember("SR001");
        trainingTopics.setUpdateDate(timestamp);
        trainingTopicsRepository.save(trainingTopics);
        return "Saved";
    }

    @GetMapping("/memberadd")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    String addMember(Model model) {
        return "addMember";
    }
}
