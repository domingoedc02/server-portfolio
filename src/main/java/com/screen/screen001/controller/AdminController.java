package com.screen.screen001.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/screen001")
public class AdminController {

    @GetMapping("/adminmenu")
    String adminMenu(Model model) {
        return "admin";
    }

    @GetMapping("/trainingadd")
    String addTraining(Model model) {
        return "addTraining";
    }

    @GetMapping("/memberadd")
    String addMember(Model model) {
        return "addMember";
    }
}
